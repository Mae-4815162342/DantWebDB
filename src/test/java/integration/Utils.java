package integration;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Utils {
  public static final String TITANIC_PATH = "/Users/diez/Documents/Fac/DantWebDB/titanic.csv";
  public static final String TAXI_PATH = "/Users/diez/Documents/Fac/DantWebDB/yellow_tripdata_2009-01.csv";
  
  public static String sendGetRequest(ResteasyWebTarget target, String body, String table, String type) {

    Response response = target
        .path("/get")
        .queryParam("table", table)
        .queryParam("type", type)
        .queryParam("fromClient", true)
        .request()
        .method("get", Entity.entity(body, MediaType.APPLICATION_JSON));
    return response.readEntity(String.class);
  }
  public static String formatJSON(Gson gson, String jsonString){
    JsonElement el = JsonParser.parseString(jsonString);
    return gson.toJson(el);
  }
}
