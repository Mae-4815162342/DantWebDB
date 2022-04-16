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
        int i = 0;
        long start = 0, middle = 0, end = 0;
        String line;
        line = br.readLine();
        while((line = br.readLine()) != null) {
           /*  start = (i == 999_999 ? System.nanoTime() : 0);
            //List<String> entry = Splitter.on(',').splitToList(line);
            //List<String> entry2 = Arrays.asList(line.split(","));
            middle = (i == 999_999 ? System.nanoTime() : 0); */
            if(!line.equals("")){
                try {
                    Worker.getInstance().insertIntoTable(line, table);
                } catch (TableNotExistsException e) {
                    return Response.status(400).entity(e.getMessage() + "\n If you meant to create it, you need to call /api/createTable\n").type("plain/text").build();
                }
            }
            //end = (i == 999_999 ? System.nanoTime() : 0);
            i++;
        }
        System.out.println("Temps d'une traduction : " + (middle - start));
        System.out.println("Temps d'une insertion : " + (end - middle));
        System.out.println("Successfully inserted " + i + " lines !");
            return Response.ok("Values from " + UPLOADED_FILE_PARAMETER_NAME + " inserted into " + tableName + "!\n").build();
    }
}

/* Successfully inserted 10000 lines !
Moyenne du temps de création de l'Arraylist : 1305
Moyenne du temps de l'insertion dans la DB : 671
 */

/* Successfully inserted 10000 lines !
Moyenne du temps de création de l'Arraylist : 3880
Moyenne du temps de l'insertion dans la DB : 775
*/
