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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@Path("/api")
public class InsertDataEndpoint {
    private static final int CHUNK_SIZE = 100;
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

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        int NB_PEERS = net.getNumberOfPeers() + 1;
        AtomicInteger nextPeer = new AtomicInteger();

        /* PRODUCER */
        buffer.lines()
                .parallel()
                .forEach(line -> {
                    if (nextPeer.get() % NB_PEERS == 0 && queue.size() > CHUNK_SIZE) {
                        // on stock dans la machine actuelle
                        try {
                            ArrayList<String> entry = new ArrayList<>(Arrays.asList(line.split(",")));
                            Worker.getInstance().insertIntoTable(tableName, entry);
                        } catch (TableNotExistsException e) {
                            e.printStackTrace();
                        }

                        nextPeer.getAndIncrement();
                    }

                    /* on ajoute une ligne à la queue */
                    queue.offer(line + "\n");
                });

        /* CONSUMERS */
        Callable<Integer> pollTask = () -> {
            ArrayList<String> chunk = new ArrayList<>();
            while (!queue.isEmpty()) {
                while (chunk.size() < CHUNK_SIZE) {
                    chunk.add(queue.poll());
                }
                // forward chunk to next peer
                nextPeer.getAndIncrement();
                System.out.println("Sending to next peer : " + nextPeer.get());
                sendChunk(chunk, tableName);
            }
            if (!chunk.isEmpty()) {
                System.out.println("Sending to next peer : " + nextPeer.get());
                sendChunk(chunk, tableName);
            }
            return null;
        };

        executorService.submit(pollTask);

        inputStream.close();
        buffer.close();
    }

    @POST
    @GZIP
    @Consumes("multipart/form-data")
    @Path("/upload")
    public Response uploadFile(@GZIP MultipartFormDataInput input) throws IOException {
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        final String TABLE_NAME = "tableName";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream));

        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            parseCSV(inputPart, tableName);
        }
        nameInputStream.close();
        return Response.ok("Values from " + UPLOADED_FILE_PARAMETER_NAME + " inserted into " + tableName + "!\n").build();
    }


    @POST
    @Path("/chunk")
    public Response insertInto(StringBuffer chunk, @QueryParam("tableName") String tableName) {
        /* INSERT INTO TABLE THE DATA*/
        String[] lines = chunk.toString().split("\n");
        for (String line : lines) {
            ArrayList<String> entry = new ArrayList<>(Arrays.asList(line.split(",")));

            try {
                Worker.getInstance().insertIntoTable(tableName, entry);
            } catch (TableNotExistsException e) {
                return Response.status(400).entity(e.getMessage() + "\n If you meant to create it, you need to call /api/createTable\n").type("plain/text").build();
            }
        }
        return Response.ok("Values inserted into " + tableName + "!\n").build();
    }
}
