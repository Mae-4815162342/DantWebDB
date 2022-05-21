package endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;

import controller.Worker;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeleteEndpoint {
  
  @DELETE
  @Path("/delete")
  public Response select(@Context UriInfo uriInfo, String jsonStr){
    // MultivaluedMap<String, String> queries  = uriInfo.getQueryParameters();
    /* String tableName = queries.get("table").get(0);
    String type = queries.get("type").get(0);
    Object res; */
    try{
      //res = Worker.getInstance().delete(jsonStr, type, tableName);
    }
    catch(Exception e){
      return Response.status(400).entity(e.getMessage() + "\n").type("plain/text").build();
    }
    return Response.ok("Unavailable").build();
  }
}
