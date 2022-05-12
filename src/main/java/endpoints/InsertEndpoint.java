package endpoints;

import controller.Worker;
import exception.TableNotExistsException;
import model.Table;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Path("/api")
@Consumes("multipart/form-data")
public class InsertEndpoint {

    @POST
    @Path("/insertInto")
    public Response insertInto(MultipartFormDataInput input) throws IOException, TableNotExistsException {
        /* GET METADATA AND FILE FROM REQUEST BODY*/
        final String TABLE_NAME = "tableName";
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream));
        System.out.println("name : " + tableName);

        final Table table = Worker.getTableByName(tableName);
        List<InputPart> fileInput = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);
        InputStream fileInputStream = fileInput.get(0).getBody(InputStream.class, null);
        InputStreamReader reader = new InputStreamReader(fileInputStream);
        BufferedReader br = new BufferedReader(reader);


        /* INSERT INTO TABLE THE DATA*/
        String line;
        line = br.readLine();
        while((line = br.readLine()) != null) {
            if(!line.equals("")){
                try {
                    Worker.getInstance().insertIntoTable(line, table);
                } catch (TableNotExistsException e) {
                    return Response.status(400).entity(e.getMessage() + "\n If you meant to create it, you need to call /api/createTable\n").type("plain/text").build();
                }
            }
        }
        return Response.ok("Values from " + UPLOADED_FILE_PARAMETER_NAME + " inserted into " + tableName + "!\n").build();
    }
    
}
