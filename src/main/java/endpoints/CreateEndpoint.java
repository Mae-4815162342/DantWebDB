package endpoints;

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
import java.util.List;
import java.util.Map;

@Path("/api/insertFile")
public class CreateEndpoint {
    final int CHUNK_SIZE = 100_000;
    final Network net = Network.getInstance();

    public void sendChunks(List<StringBuffer> buffers, String tableName) {
        //envoi de requête /insertIntoTable
        System.out.println("ON VIDE");
        for (StringBuffer buffer : buffers) {
            buffer.delete(0, buffer.length());
            net.getInstance().sendDataToPeer(buffer, tableName, '/insertIntoTable', MediaType.APPLICATION_JSON);
        }
    }

    public byte[] parseCSV(InputPart inputPart) throws IOException {
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));

        /* stock chunk of csv file */
        List<StringBuffer> buffers = new ArrayList<StringBuffer>();
        for (int j = 0; j < 5; j++) {
            buffers.add(new StringBuffer());
        }

        String line;
        int i = 0;
        int countLine = 0;
        while ((line = buffer.readLine()) != null) {
            if (countLine < CHUNK_SIZE) {
                buffers.get(i % 5).append(line + "\n");
                countLine++;
            } else {
                i++;
                if ((i % 5) == 0) {
                    sendChunks(buffers);
                } else {
                    buffers.get(i % 5).append(line + "\n");
                    countLine++;
                }
            }
        }
        sendChunks(buffers);
        return null;
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
