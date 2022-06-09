package endpoints;

import controller.Worker;
import exception.TableNotExistsException;
import model.Table;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


@Path("/api")
public class InsertDataEndpoint {
    private static final int CHUNK_SIZE = 50_000;
    private static final int MAX_LINES = 1_000_000;
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
    public void parseCSV(InputPart inputPart, String tableName, int limit) throws IOException, TableNotExistsException, InterruptedException {
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        int NB_PEERS = Network.getInstance().getNumberOfPeers();
        System.out.println("NB_PEERS : " + NB_PEERS);
        ExecutorService executorService = Executors.newFixedThreadPool(NB_PEERS + 1);
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
		AtomicInteger NB_LINES = new AtomicInteger();
        Table table = Worker.getInstance().getTableByName(tableName);
        System.out.println("Running async offer task...");
        buffer.readLine();
        /* offer task */
        /* PRODUCER */
        int i = 1;
        int j = 0;
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
            return null;
        };
        try {
            String line;
            int nbLines = 0;
            while ((line = buffer.readLine()) != null) {
                if(nbLines > limit) break;
                if(!line.equals("")){
                    nbLines ++;
                    if(j != MAX_LINES){
                        i++;
                        if(i % (NB_PEERS + 1) == 0){
                            Worker.getInstance().insertIntoTable(tableName, line);
                        }
                        else{
                            queue.offer(line);
                            j++;
                        }
                    }
                    else{
                        executorService.submit(pollTask);
                        while(!queue.isEmpty()){
        
                        }
                        j = 0;
                        queue.clear();
                    }
                } 
            }
            if(j!=0 && nbLines < limit){
                executorService.submit(pollTask);
                while(!queue.isEmpty()){

                }
                queue.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        inputStream.close();
        buffer.close();
    }

    @POST
    @GZIP
    @Consumes("multipart/form-data")
    @Path("/upload")
    public Response uploadFile(@GZIP MultipartFormDataInput input) throws IOException, TableNotExistsException, InterruptedException {
        System.out.println("Receive request on /upload");
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        final String TABLE_NAME = "tableName";
        final String LIMIT = "limit";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        System.out.println("Creating stream");
        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream));

        List<InputPart> limitInput = uploadForm.get(LIMIT);
        InputStream limitInputStream = limitInput.get(0).getBody(InputStream.class, null);
        int limit = Integer.parseInt(new String(IOUtils.toByteArray(limitInputStream)));

        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            System.out.println("Parsing CSV");
            parseCSV(inputPart, tableName, limit);
        }
        nameInputStream.close();
        limitInputStream.close();
        return Response.ok("Values from " + UPLOADED_FILE_PARAMETER_NAME + " inserted into " + tableName + "!").build();
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
