package endpoints;

import controller.Worker;
import exception.TableNotExistsException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Path("/api")
@Consumes("multipart/form-data")
public class InsertEndpoint {

    @POST
    @Path("/insertInto")
    public Response insertInto(MultipartFormDataInput input) throws IOException {
        /* GET METADATA AND FILE FROM REQUEST BODY*/
        final String TABLE_NAME = "tableName";
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream));
        System.out.println("name : " + tableName);

        List<InputPart> fileInput = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);
        InputStream fileInputStream = fileInput.get(0).getBody(InputStream.class, null);
        InputStreamReader reader = new InputStreamReader(fileInputStream);
        BufferedReader br = new BufferedReader(reader);

        /* INSERT INTO TABLE THE DATA*/
        int i = 0;
        String line;
        while((line = br.readLine()) != null && i < 12_000_000 ) {
            ArrayList<String> entry = new ArrayList<>(Arrays.asList(line.split(";")));

            try {
                Worker.getInstance().insertIntoTable(tableName, entry);
            } catch (TableNotExistsException e) {
                return Response.status(400).entity(e.getMessage() + "\n If you meant to create it, you need to call /api/createTable\n").type("plain/text").build();
            }

            i++;
        }
        System.out.println("Successfully inserted " + i + " lines !");
        return Response.ok("Values from " + UPLOADED_FILE_PARAMETER_NAME + " inserted into " + tableName + "!\n").build();
    }
}
