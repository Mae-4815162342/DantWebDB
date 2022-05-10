package endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
public class SelectEndpoint {
  
  @GET
  @Path("/get")
  public Response select(@Context UriInfo uriInfo, String jsonStr){
    MultivaluedMap<String, String> queries  = uriInfo.getQueryParameters();
    String tableName = queries.get("table").get(0);
    String type = queries.get("type").get(0);
    Object res;
    try{
      res = Worker.getInstance().select(jsonStr, type, tableName);
    }
    catch(Exception e){
      return Response.status(400).entity(e.getMessage() + "\n").type("plain/text").build();
    }
    return Response.ok(res).build();
  }

  @GET
  @Path("/get/columns")
  public Response getColumns(@Context UriInfo uriInfo) {
    MultivaluedMap<String, String> queries  = uriInfo.getQueryParameters();
    String tableName = queries.get("table").get(0);
    Object res;
    try{
      res = Worker.getInstance().getColumns(tableName);
    }
    catch(Exception e){
      return Response.status(400).entity(e.getMessage() + "\n").type("plain/text").build();
    }
    return Response.ok(res).build();
  }
}
