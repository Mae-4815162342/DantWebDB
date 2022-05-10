package endpoints;

import controller.Worker;
import exception.TableExistsException;
import model.Table;
import network.Network;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import filter.GsonProvider;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
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

        try {
            if (!fromClient) {
                System.out.println("Receiving from a peer a request to create " + TABLE_NAME);
            }
            /* ajout dans la database */
            Worker.getInstance();
            Worker.createTable(TABLE_NAME,COLUMNS);

        } catch(TableExistsException e) {
            return Response.status(400).entity(e.getMessage()).type("plain/text").build();
        }

        if (fromClient) {
            // send request to peers
            ArrayList<String> peers = Network.getInstance().getPeersIPAdressesList();
            ResteasyClient client = new ResteasyClientBuilder().build();
            String path = "http://{ipAddress}:8080/api";
            client.register(GsonProvider.class);
            ResteasyWebTarget target = client.target(UriBuilder.fromPath(path));

            for(String ipAddress : peers){
                System.out.println("Sending to " + ipAddress);

                Response response = target
                        .path("/table-json")
                        .resolveTemplate("ipAddress", ipAddress)
                        .queryParam("fromClient", false)
                        .request()
                        .post(Entity.entity(input, MediaType.APPLICATION_JSON));

                System.out.println(response.getStatus());
                response.close();
                /* TODO: code refacto : sendPostToPeers / sendGetToPeers / sendPutToPeers
                * need : MediaType.APPLICATION_JSON / MediaType.MULTIPART_FORM_DATA_TYPE
                * the parameter (csv file for insert, json request for create, select...)
                * queryParam 'from client'
                * */
            }

            return Response.ok("Table created:" + TABLE_NAME + " \n With columns : " + COLUMNS).build();
        }
        return Response.ok("Table created:" + TABLE_NAME + " \n With columns : " + COLUMNS).build();
    }
}
