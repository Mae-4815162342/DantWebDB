package endpoints;

import controller.Worker;
import exception.TableExistsException;
import model.Table;
import network.Network;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.*;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TableEndpoint {

    @POST
    @Path("/table-json")
    public Response createTableFromJson(Table input,  @QueryParam("fromClient") boolean fromClient) {
        final String TABLE_NAME = input.getTableName();
        final HashMap<String, String> COLUMNS =  input.getColumns();

        if (fromClient) {
            try {
                /* ajout dans la database */
                Worker.getInstance();
                Worker.createTable(TABLE_NAME,COLUMNS);

                // send request to peers
                ArrayList<String> peers = Network.getInstance().getPeersIPAdressesList();
                for(String ipAddress : peers){
                    String path = "http://" + ipAddress + ":8080/table-json";
                    ResteasyClient client = new ResteasyClientBuilder().build();
                    ResteasyWebTarget target = client.target(UriBuilder.fromPath(path));
                    TableEndpoint proxy = target.proxy(TableEndpoint.class);
                    System.out.println("Sending to " + ipAddress);
                    return proxy.createTableFromJson(input, false);
                }

                return Response.ok("Table created:" + TABLE_NAME + " \n With columns : " + COLUMNS).build();

            } catch(TableExistsException e) {
                return Response.status(400).entity(e.getMessage()).type("plain/text").build();
            }
        }

        else {
            try {
                System.out.println("Receiving from a peer a request to create " + TABLE_NAME);
                /* ajout dans la database */
                Worker.getInstance();
                Worker.createTable(TABLE_NAME,COLUMNS);

                return Response.ok("Table created:" + TABLE_NAME + " \n With columns : " + COLUMNS).build();

            } catch(TableExistsException e) {
                return Response.status(400).entity(e.getMessage()).type("plain/text").build();
            }
        }
    }
}
