package endpoints;

import controller.Worker;
import exception.TableExistsException;
import model.Table;
import model.TableTree;
import network.Network;
import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TableEndpoint {

    @POST
    @Path("/table-json")
    public Response createTableFromJson(TableTree input, @QueryParam("fromClient") boolean fromClient) {
        final String TABLE_NAME = input.getName();
        final LinkedHashMap<String,String> COLUMNS =  input.getColumns();
        String responseMessage = "Table created:" + TABLE_NAME + " \n With columns : " + COLUMNS;
        if(input==null) System.out.println("NULLLLLLL");
        try {
            if (!fromClient) {
                System.out.println("Receiving from a peer a request to create " + TABLE_NAME);
            }
            /* ajout dans la database */
            Worker.getInstance();
            Worker.createTable(input);

            /* send to peers */
            if (fromClient) {
                String path = "/table-json";
                return Network.getInstance().sendPostRequest(path, input, MediaType.APPLICATION_JSON, responseMessage);
            }
        } catch(TableExistsException e) {
            return Response.status(400).entity(e.getMessage()).type("plain/text").build();
        }
        return Response.ok("ici " + responseMessage).build();
    }
}