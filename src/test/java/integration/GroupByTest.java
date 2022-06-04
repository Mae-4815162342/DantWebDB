package integration;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.MediaType;
import integration.FilesPath;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import filter.GsonProvider;

public class GroupByTest {
  private ResteasyClient client;
  private final String baseURI = "http://localhost:8081/api";
  private ResteasyWebTarget target;
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Before
  public void setUp() {
    this.client = new ResteasyClientBuilder().build();
    this.client.register(GsonProvider.class);
    this.target = client.target(UriBuilder.fromPath(baseURI));
    String input = "{\n  \"tableName\": \"titanic4\",\n  \"columns\":{\n    \"PassengerId\": \"int\",\n    \"Survived\": \"string\",\n        \"Pclass\": \"string\",\n        \"Name\": \"string\",\n        \"Sex\": \"string\",\n        \"Age\": \"int\",\n        \"SibSp\": \"string\",\n        \"Parch\": \"string\",\n        \"Ticket\": \"string\",\n        \"Fare\": \"string\",\n        \"Cabin\": \"string\",\n        \"Embarked\": \"string\"\n  }\n}";
    System.out.println("• Sending post request to localhost");
  
    Response response = target
          .path("/table-json")
          .queryParam("fromClient", true)
          .request()
          .post(Entity.entity(input, MediaType.APPLICATION_JSON));
    if(!response.readEntity(String.class).equals("titanic4 already exists in the database !")){
      MultipartFormDataOutput form = new MultipartFormDataOutput();
      File csv = new File(FilesPath.TITANIC_PATH);
      form.addFormData("tableName", "titanic4", MediaType.TEXT_PLAIN_TYPE);
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
  public String sendGetRequest(String body, String table, String type) {

    Response response = target
        .path("/get")
        .queryParam("table", table)
        .queryParam("type", type)
        .request()
        .method("get", Entity.entity(body, MediaType.APPLICATION_JSON));
    return response.readEntity(String.class);
  }
  public String formatJSON(String jsonString){
    JsonElement el = JsonParser.parseString(jsonString);
    return gson.toJson(el);
  }

  @Test
  public void groupBy() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"by\":[\"Sex\",\"Survived\"],\"_count\":[\"_all\"]}"), "titanic4", "groupBy");
    String expected = "[{\"Survived\":\"0\",\"_count\":{\"_all\":81.0},\"Sex\":\"female\"},{\"Survived\":\"1\",\"_count\":{\"_all\":233.0},\"Sex\":\"female\"},{\"Survived\":\"1\",\"_count\":{\"_all\":109.0},\"Sex\":\"male\"},{\"Survived\":\"0\",\"_count\":{\"_all\":468.0},\"Sex\":\"male\"}]";
    assertEquals(expected, actual);
  }

  @Test
  public void groupByWithHaving() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"by\":[\"Sex\",\"Survived\"],\"_count\":[\"_all\"],\"having\":{\"_count\":{\"_all\":{\"gt\":150}}}}"), "titanic4", "groupBy");
    String expected = "[{\"Survived\":\"1\",\"_count\":{\"_all\":233.0},\"Sex\":\"female\"},{\"Survived\":\"0\",\"_count\":{\"_all\":468.0},\"Sex\":\"male\"}]";
    assertEquals(expected, actual);
  }
}
