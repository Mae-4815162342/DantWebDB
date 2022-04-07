package endpoints;

import controller.Worker;
import exception.TableExistsException;
import model.Table;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Path("/api")
@Consumes("multipart/form-data")
public class TableEndpoint {
    @POST
    @Path("/table")
    public Response createTable(MultipartFormDataInput input) throws IOException {
        // parse les paramètres du input 
        // -> envoyer au worker Worker.createTable(tableName, tutti quanti)
        final String TABLE_NAME = "name";
        final String NUMBER_OF_COLUMNS= "columns";

        ArrayList<String> res = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);

        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream)).toUpperCase();
        res.add("name : " + tableName);

        List<InputPart> colNumberInput = uploadForm.get(NUMBER_OF_COLUMNS);
        InputStream colNumberInputStream = colNumberInput.get(0).getBody(InputStream.class, null);
        String colNumberString = new String(IOUtils.toByteArray(colNumberInputStream));
        res.add("number of columns : " + colNumberString);
        int colNumber = Integer.parseInt(colNumberString);

        // creer les columns dans la table logic -> worker 
        if(colNumber > 0) {
            for (int i = 1; i <= colNumber; i++) {
                List<InputPart> columnInput = uploadForm.get("" + i);
                InputStream columnInputStream = columnInput.get(0).getBody(InputStream.class, null);
                String column = new String(IOUtils.toByteArray(columnInputStream));
                String[] columnParameters = column.split(";");
                columns.add(columnParameters[0].toUpperCase() + " (Type: " + columnParameters[1] + ")");
            }
        } else {
            return Response.status(400).entity("Bad number of columns!").type("plain/text").build();
        }

        return Response.ok("Table created\n" + res + "\n" + columns).build();
    }

    @POST
    @Path("/table-json")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTableFromJson(Table input) {
        /* récupération des informations de la table */
        final String TABLE_NAME = input.getTableName();
        final HashMap<String, String> COLUMNS =  input.getColumns();

        try {
            /* ajout dans la database */
            Worker.createTable(TABLE_NAME,COLUMNS);
            return Response.ok("Table created\n" + TABLE_NAME + "\n" + COLUMNS).build();

        } catch(TableExistsException e) {
            return Response.status(400).entity(e).type("plain/text").build();
        }
    }
}
