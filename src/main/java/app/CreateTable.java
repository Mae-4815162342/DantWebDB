package app;

import model.ClientTable;
import model.Structure;
import model.Tuple;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Path("/api")
@Consumes("multipart/form-data")
public class CreateTable {
    @POST
    @Path("/createTable")
    public Response createTable(MultipartFormDataInput input) throws IOException {
        final String TABLE_NAME = "name";
        final String NUMBER_OF_COLUMNS= "columns";
        ArrayList<Object> res = new ArrayList<>();
        ArrayList<Tuple> columns = new ArrayList<>();
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream)).toUpperCase();
        res.add("name : " + tableName);

        ClientTable oldTable = Structure.getTableByName(tableName);
        if(oldTable != null) {
            return Response.status(400).entity("This table(" + tableName + ") already exist!\n" + oldTable.toString()).type("plain/text").build();
        }

        List<InputPart> colNumberInput = uploadForm.get(NUMBER_OF_COLUMNS);
        InputStream colNumberInputStream = colNumberInput.get(0).getBody(InputStream.class, null);
        String colNumberString = new String(IOUtils.toByteArray(colNumberInputStream));
        res.add("number of columns : " + colNumberString);
        int colNumber = Integer.parseInt(colNumberString);

        if(colNumber > 0) {
            for (int i = 1; i <= colNumber; i++) {
                List<InputPart> columnInput = uploadForm.get("" + i);
                InputStream columnInputStream = columnInput.get(0).getBody(InputStream.class, null);
                String column = new String(IOUtils.toByteArray(columnInputStream));
                String[] columnParameters = column.split(";");
                columns.add(new Tuple(columnParameters[0].toUpperCase(),columnParameters[1]));
            }
            res.add("columns : " + columns.toString());
            Structure.createTable(tableName, columns);
        } else {
            return Response.status(400).entity("Bad number of columns!").type("plain/text").build();
        }
        return Response.ok("Table created!\n" + Structure.showTables()).build();
    }
}
