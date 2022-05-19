package endpoints;

import controller.Worker;
import exception.TableNotExistsException;
import network.Network;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Path("/api")
public class InsertDataEndpoint {
    private static final int CHUNK_SIZE = 100_000;
    private final Network net = Network.getInstance();

    public void sendChunk(ArrayList<String> buffer, String tableName) {
        System.out.println("Sending chunk");
        //forward chunk to a peers
        net.sendDataToPeer(buffer, tableName, "/chunk", MediaType.APPLICATION_JSON);
    }

    /* idea:
     * - multiple producer that adds lines in the queue
     * - mutliple consumers thats takes out lines
     *       --> if the consumer has X lines it send a chunk to next peer
     * */
    public void parseCSV(InputPart inputPart, String tableName) throws IOException {
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        int NB_PEERS = Network.getInstance().getNumberOfPeers();
        ExecutorService executorService = Executors.newFixedThreadPool(NB_PEERS);
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        AtomicInteger NB_LINES = new AtomicInteger();

        System.out.println("Running async offer task...");

        /* offer task */
        CompletableFuture.runAsync(() -> {
            /* PRODUCER */
            try {
                int i = 1;
                String line;
                while ((line = buffer.readLine()) != null) {
                    i++;
                    // on insert la ligne en local
                    if (i % NB_PEERS == 0) {
                        Worker.getInstance().insertIntoTable(tableName, line);
                    }
                    // on ajoute une ligne à la queue
                    else {
                        queue.offer(line + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        while (queue.isEmpty()) {
            System.out.println("Waiting for queue to have line");
        }

        /* CONSUMERS */
        Callable<Integer> pollTask = () -> {
            System.out.println("poll task created");
            ArrayList<String> chunk = new ArrayList<>();
            while (!queue.isEmpty()) {
                while (chunk.size() < CHUNK_SIZE && !queue.isEmpty()) {
                    chunk.add(queue.poll());
                }
                // forward chunk to next peer
                sendChunk(chunk, tableName);
                chunk.clear();
            }
            sendChunk(chunk, tableName);
            chunk.clear();
            return null;
        };

        try {
            System.out.println("Submitting poll task...");
            executorService.submit(pollTask);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            while (!queue.isEmpty()) {
                System.out.println("waiting for insert to finish");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            inputStream.close();
            buffer.close();
        }
    }

    @POST
    @GZIP
    @Consumes("multipart/form-data")
    @Path("/upload")
    public Response uploadFile(@GZIP MultipartFormDataInput input) throws IOException {
        System.out.println("Receive request on /upload");
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        final String TABLE_NAME = "tableName";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        System.out.println("Creating stream");
        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream));

        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            System.out.println("Parsing CSV");
            parseCSV(inputPart, tableName);
        }
        nameInputStream.close();
        return Response.ok("Values from " + UPLOADED_FILE_PARAMETER_NAME + " inserted into " + tableName + "!\n").build();
    }


    @POST
    @Path("/chunk")
    public Response insertInto(ArrayList<String> chunk, @QueryParam("tableName") String tableName) {
        /* INSERT INTO TABLE THE DATA*/
        try {
            System.out.println("Received a chunk to insert into + " + tableName);
            Worker.getInstance().insertChunkIntoTable(tableName, chunk);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).entity(e.getMessage() + "\n If you meant to create it, you need to call /api/createTable\n").type("plain/text").build();
        }
        return Response.ok("Values inserted into " + tableName + "!\n").build();
    }
}
