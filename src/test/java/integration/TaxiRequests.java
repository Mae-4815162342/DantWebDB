package integration;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static integration.Utils.sendGetRequest;
import static integration.Utils.formatJSON;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import filter.GsonProvider;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaxiRequests {
  private ResteasyClient client;
  private final String baseURI = "http://localhost:8081/api";
  private ResteasyWebTarget target;
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Before
  public void setUp() {
    this.client = new ResteasyClientBuilder().build();
    this.client.register(GsonProvider.class);
    this.target = client.target(UriBuilder.fromPath(baseURI));
    String input = formatJSON(gson, "{\"tableName\":\"taxi\",\"columns\":{\"VendorID\":\"int\",\"tpep_pickup_datetime\":\"string\",\"tpep_dropoff_datetime\":\"string\",\"passenger_count\":\"int\",\"trip_distance\":\"string\",\"RatecodeID\":\"string\",\"store_and_fwd_flag\":\"string\",\"PULocationID\":\"int\",\"DOLocationID\":\"int\",\"payment_type\":\"string\",\"fare_amount\":\"string\",\"extra\":\"int\",\"mta_tax\":\"string\",\"tip_amount\":\"int\",\"tolls_amount\":\"string\",\"improvement_surcharge\":\"string\",\"total_amount\":\"string\",\"congestion_surcharge\":\"string\"}}");
    System.out.println("• Sending post request to localhost");
  
    Response response = target
          .path("/table-json")
          .queryParam("fromClient", true)
          .request()
          .post(Entity.entity(input, MediaType.APPLICATION_JSON));
    if(!response.readEntity(String.class).equals("taxi already exists in the database !")){
      MultipartFormDataOutput form = new MultipartFormDataOutput();
      File csv = new File(Utils.TAXI_PATH);
      form.addFormData("tableName", "taxi", MediaType.TEXT_PLAIN_TYPE);
      form.addFormData("file", csv, MediaType.APPLICATION_OCTET_STREAM_TYPE);
      target
          .path("/upload")
          .request()
          .post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
    }
  }

  @After
  public void teardown() {
    this.client.close();
  }
  @Test 
  public void A_ResetTimer(){
    assertEquals(true, true);
  }
  @Test
  public void Request2(){
    int actual = sendGetRequest(target, formatJSON(gson, "{\"limit\":1000}"), "taxi", "findMany").length();
    int expected = 509998;
    assertEquals(expected, actual);
  }
  @Test
  public void Request4(){
    int actual = sendGetRequest(target, formatJSON(gson, "{\"by\":[\"trip_distance\",\"tpep_pickup_datetime\"],\"limit\":1000}"), "taxi", "groupBy").length();
    int expected = 79434;
    assertEquals(expected, actual);
  }
}
