package endpoints;

import com.sun.mail.iap.ByteArray;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Path("/api")
public class InsertDataEndpoint {
    private static final int CHUNK_SIZE = 100_000;
    private final Network net = Network.getInstance();

    public void sendChunk(StringBuffer buffer, String tableName) {
        System.out.println("Sending chunk");
        //forward chunk to a peers
        net.sendDataToPeer(buffer, tableName, "/chunk", MediaType.APPLICATION_JSON);
        //vider le buffer
        buffer.delete(0, buffer.length());
    }

    public void parseCSV(InputPart inputPart, String tableName) throws IOException {
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer bufferToSend = new StringBuffer();
        String line;
        int NB_PEERS = net.getNumberOfPeers() + 1;
        System.out.println(NB_PEERS);
        int countLine = 0;
        int peers = 0;
        while ((line = buffer.readLine()) != null) {
            if (peers % NB_PEERS == 0) {
                // on stock dans la machine actuelle
                try {
                    ArrayList<String> entry = new ArrayList<>(Arrays.asList(line.split(",")));
                    Worker.getInstance().insertIntoTable(tableName, entry);
                } catch (TableNotExistsException e) {
                    e.printStackTrace();
                }
                countLine++;
                if (countLine >= CHUNK_SIZE) {
                    peers++;
                    countLine = 1;
                }
            } else {
                bufferToSend.append(line + "\n");
                if (countLine < CHUNK_SIZE) {
                    countLine++;
                } else {
                    //on envoie à un des peers
                    System.out.println("Chunk rempli");
                    sendChunk(bufferToSend, tableName);
                    countLine = 1;
                    peers++;
                }
            }
        }
        System.out.println("last chunk");
        sendChunk(bufferToSend, tableName);
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
