package endpoints;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;

import controller.Worker;
import exception.*;
import network.Network;
import utils.SendTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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
    String fromClient = queries.get("fromClient").get(0);
    System.out.println(tableName + " " + type + " " + fromClient);
    Worker worker = Worker.getInstance();
    Object res = null;
    try {
      //setting the request in the Worker in order to get data from it
      System.out.println("Setting request");
      worker.setRequest(jsonStr, type, tableName, fromClient.equals("true"));
      int tableSize = worker.getTableByName(tableName).getTableSize();

      //checking local selection params :
      //if the request comes from an other node or the request has a limit inferior to the number of lines in the local table without select or group by or aggregation,
      //we don't need to call other nodes
      if((!worker.reqHasWhere() && !worker.reqIsGroupBy() &&!worker.reqIsAggregate() && worker.reqLimit() < tableSize && worker.reqLimit() > 0)) {
        Worker.getInstance().select();
        res = Worker.getInstance().getRes();
        //else all the nodes need to process the requestq
      } else {
        System.out.println("Sending to all");
        res = selectRequestToAll(worker, jsonStr, tableName, type);
      }
    }
    catch(Exception e){
      return Response.status(400).entity(e.getMessage() + "\n").type("plain/text").build();
    }
    return Response.ok(res).build();
  }

  @POST
  @Path("/get-machine")
  //used only between nodes
  public Response selectMachine(@Context UriInfo uriInfo, String jsonStr){
    MultivaluedMap<String, String> queries  = uriInfo.getQueryParameters();
    String tableName = queries.get("table").get(0);
    String type = queries.get("type").get(0);
    String fromClient = queries.get("fromClient").get(0);
    Worker worker = Worker.getInstance();
    Object res = null;
    try {
      //setting the request in the Worker in order to get data from it
      worker.setRequest(jsonStr, type, tableName, fromClient.equals("true"));
      Worker.getInstance().select();
      res = Worker.getInstance().getRes();
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
      List<Future<Void>> answers = new ArrayList<>();
      for(int i = 0; i < numberOfPeers; i++) {
        //local select
        if(i == numberOfPeers - 1) {
          System.out.println("Adding local thread");
          answers.add(executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws SelectException {
              try {
                worker.select();
              }catch (Exception e) {
                throw new SelectException(jsonStr);
              }
              return null;
            }
          }));
          //sending selects
        } else {
          String ipAddress = Network.getInstance().getIpAdressFromIndex(i);
          System.out.println("Sending to " + ipAddress);
          SendTask st = new SendTask(ipAddress, jsonStr, tableName, type);
          answers.add(executorService.submit(st));
        }
      }

      //waiting for all answers to finish
      for(Future future: answers) {{
        future.get();
      }}
      executorService.shutdown();

    }catch (Exception e) {
      throw new SelectException(jsonStr);
    }
    return worker.getRes();
  }

  @GET
  @Path("/get/metadata")
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
