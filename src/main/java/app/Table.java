package app;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import filter.GsonProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
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
    Map<String, String> StringBytetoStringString(Map<String, ByteBuffer> lines) {
      Map<String, String> line = new HashMap<String, String>();
      for(String key : App.headers){
        String value = "";
        if(lines.get(key)!=null){
          value = new String(lines.get(key).array());;
        }
        line.put(key, value);
      }
      return line;
    }
    @GET
    public Response get(){
      String res = new String(App.data.array(), 0, App.data.position());
      return Response.ok(res).build();
    }
    @GET
    @Path("/query")
    public Response get(@Context UriInfo uriInfo){
      Set<Map<String, String>> res = new HashSet<Map<String,String>>();
      MultivaluedMap<String, String> queries  = uriInfo.getQueryParameters();
      for(Map<String, ByteBuffer> lines : App.data){
        for(String key : queries.keySet()){
          String value = "";
          if(lines.get(key)!=null){
            value = new String(lines.get(key).array());;
          }          
          if(queries.get(key).get(0).equals(value)){
            res.add(StringBytetoStringString(lines));
          }
        }
      }
      return Response.ok(res).build();
    }
}
