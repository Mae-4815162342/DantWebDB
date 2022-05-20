package endpoints;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;

import controller.Worker;
import exception.SelectException;
import network.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SelectEndpoint {
  private class SendTask implements Callable<Object> {
    private String ipAddress;
    private String json;
    private String table;
    private String type;
    private SendTask(String ipAsdress, String json, String table, String type) {
      this.ipAddress = ipAddress;
      this.json = json;
      this.table = table;
      this.type = type;
    }
    public Object call() throws Exception {
      Worker.getInstance().addLinesToAnswer(Network.getInstance().sendSelectToPeer(ipAddress, json, table, type, "/api/get", "application/json"));
      return null;
    }
  };
  
  @GET
  @Path("/get")
  public Response select(@Context UriInfo uriInfo, String jsonStr){
    MultivaluedMap<String, String> queries  = uriInfo.getQueryParameters();
    String tableName = queries.get("table").get(0);
    String type = queries.get("type").get(0);
    String fromClient = queries.get("fromClient").get(0);
    Worker worker = Worker.getInstance();
    Object res = null;
    try {
      //setting the request in the Worker in order to get data from it
      worker.setRequest(jsonStr, type, tableName);
      int tableSize = worker.getTableByName(tableName).getTableSize();
      //checking local selection params
      if(fromClient.equals("false") || (!worker.reqHasSelect() && !worker.reqHasWhere() && !worker.reqHasGroupBy() && worker.reqLimit() < tableSize)) {
        res = Worker.getInstance().select(tableName);
      //else all need to process
      } else {
        res = selectRequestToAll(worker, jsonStr, tableName, type);
      }
    }
    catch(Exception e){
      return Response.status(400).entity(e.getMessage() + "\n").type("plain/text").build();
    }
    return Response.ok(res).build();
  }

  private Object selectRequestToAll(Worker worker, String jsonStr, String tableName, String type) throws Exception {
    try {
      int numberOfPeers = Network.getInstance().getNumberOfPeers() + 1;
      ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
      List<Future<Object>> answers = new ArrayList<>();
      for(int i = 0; i < numberOfPeers; i++) {
        String ipAddress = Network.getInstance().getIpAdressFromIndex(i);
        SendTask st = new SendTask(ipAddress, jsonStr, tableName, type);
        answers.add(executorService.submit(st));
      }



    }catch (Exception e) {
      throw new SelectException(jsonStr);
    }
    return worker.getRes();
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
