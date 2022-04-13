package endpoints;

import controller.Worker;
import exception.TableNotExistsException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Path("/api/select")
public class SelectEndpoint {

    @GET
    @Path("/equals")
    public Response getRowsfromSimpleReq(MultipartFormDataInput input) throws IOException {
        /* GET METADATA AND FILE FROM REQUEST BODY*/
        final String TABLE_NAME = "tableName";
        final String REQUEST = "request";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> tableNameInput = uploadForm.get(TABLE_NAME);
        InputStream nameInputStream = tableNameInput.get(0).getBody(InputStream.class, null);
        String tableName = new String(IOUtils.toByteArray(nameInputStream));
        System.out.println("name : " + tableName);

        List<InputPart> requestInput = uploadForm.get(REQUEST);
        InputStream requestInputStream = requestInput.get(0).getBody(InputStream.class, null);
        String request = new String(IOUtils.toByteArray(requestInputStream));
        System.out.println("request : " + request);

        ArrayList<String> res;
        /*Get rows from table*/
        try {
            res = Worker.getInstance().getRowsFromTableWithSimpleParam(tableName, request);
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).type("plain/text").build();
        }

        return Response.ok(res).build();
    }
}
