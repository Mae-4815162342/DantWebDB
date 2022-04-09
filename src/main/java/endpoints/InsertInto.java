package endpoints;

import storage.*;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import model.Database;
import model.Table;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Path("/api")
@Consumes("multipart/form-data")
public class InsertInto {
    @POST
    @Path("/insertInto")
    public Response insertInto(MultipartFormDataInput input) throws IOException {
        final String TABLE_NAME = "tableName";
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream)).toUpperCase();
        System.out.println("name : " + tableName);

        SarahStorage MaelysStorage = Database.getTableByName(tableName);
        if(MaelysStorage == null) {
            return Response.status(400).entity("This table(" + tableName + ") does not exist!\nIf you meant to create it, you need to call /api/createTable\n").type("plain/text").build();
        }

        List<InputPart> fileInput = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);
        InputStream fileInputStream = fileInput.get(0).getBody(InputStream.class, null);
        byte[] bytes = IOUtils.toByteArray(fileInputStream );
        String fic = new String(bytes);
        fic = fic.replaceAll("\\r", "");
        String[] lines = fic.split("\n");

        for(String line: lines) {
            MaelysStorage.insert(line);
        }

        return Response.ok("Values inserted into " + tableName + "!\n" + MaelysStorage.getData()).build();
    }
}
