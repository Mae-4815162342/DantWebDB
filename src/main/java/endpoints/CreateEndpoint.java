package endpoints;

import com.sun.mail.iap.ByteArray;
import controller.Worker;
import exception.TableNotExistsException;
import network.Network;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.GZIP;
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

@Path("/api/insertFile")
public class CreateEndpoint {
    final int CHUNK_SIZE = 100_000;
    final Network net = Network.getInstance();

    public void sendChunk(StringBuffer buffer, String tableName) {
        net.sendDataToPeer(buffer, tableName, "/insertIntoTable", MediaType.APPLICATION_JSON);
        //vider le buffer
        buffer.delete(0, buffer.length());
    }

    public void parseCSV(InputPart inputPart, String tableName) throws IOException {
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer bufferToSend = new StringBuffer();
        String line;
        int NB_PEERS = net.getNumberOfPeers();
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
                }
            } else {
                bufferToSend.append(line + "\n");
                if (countLine < CHUNK_SIZE) {
                    countLine++;
                } else {
                    //on envoie à un des peers
                    sendChunk(bufferToSend, tableName);
                    countLine = 1;
                    peers++;
                }
            }
        }
        sendChunk(bufferToSend, tableName);
    }

    @POST
    @GZIP
    @Consumes("multipart/form-data")
    public Response post(@GZIP MultipartFormDataInput input) throws IOException {
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        final String TABLE_NAME = "tableName";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream));

        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            parseCSV(inputPart, TABLE_NAME);
        }
        return Response.ok("Values from " + UPLOADED_FILE_PARAMETER_NAME + " inserted into " + tableName + "!\n").build();
    }
}
