package app;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import filter.GsonProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Produces(MediaType.APPLICATION_JSON)
@Path("/api/table")
public class Table {
    @GET
    public Response get() {
      return Response.ok(App.data).build();
    }
    @GET
    @Path("/query")
    public Response get(@Context UriInfo uriInfo){
      Set<Map<String, String>> res = new HashSet<Map<String,String>>();
      MultivaluedMap<String, String> queries  = uriInfo.getQueryParameters();
      System.out.println("Yo !");
      for(Map<String, String> lines : App.data){
        for(String key : queries.keySet()){
          if(queries.get(key).get(0).equals(lines.get(key))){
            res.add(lines);
          }
        }
      }
      return Response.ok(res).build();
    }
}
