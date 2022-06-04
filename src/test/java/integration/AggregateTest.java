package integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

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
import org.junit.Test;

import filter.GsonProvider;

public class AggregateTest {
  private ResteasyClient client;
  private final String baseURI = "http://localhost:8081/api";
  private ResteasyWebTarget target;

  @Before
  public void setUp() {
    this.client = new ResteasyClientBuilder().build();
    this.client.register(GsonProvider.class);
    this.target = client.target(UriBuilder.fromPath(baseURI));
    String input = "{\n  \"tableName\": \"titanic1\",\n  \"columns\":{\n    \"PassengerId\": \"int\",\n    \"Survived\": \"string\",\n        \"Pclass\": \"string\",\n        \"Name\": \"string\",\n        \"Sex\": \"string\",\n        \"Age\": \"int\",\n        \"SibSp\": \"string\",\n        \"Parch\": \"string\",\n        \"Ticket\": \"string\",\n        \"Fare\": \"string\",\n        \"Cabin\": \"string\",\n        \"Embarked\": \"string\"\n  }\n}";
    System.out.println("• Sending post request to localhost");
  
    Response response = target
          .path("/table-json")
          .queryParam("fromClient", true)
          .request()
          .post(Entity.entity(input, MediaType.APPLICATION_JSON));
    if(!response.readEntity(String.class).equals("titanic1 already exists in the database !")){
      MultipartFormDataOutput form = new MultipartFormDataOutput();
      File csv = new File(FilesPath.TITANIC_PATH);
      form.addFormData("tableName", "titanic1", MediaType.TEXT_PLAIN_TYPE);
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

  @Test
  public void AggregateCount() throws IOException {
    String actual = sendGetRequest("{\n  \"_count\": [\n    \"Cabin\", \"Age\", \"_all\"\n  ]\n}", "titanic1",
        "Aggregate");
    String expected = "{\"_count\":{\"Cabin\":204.0,\"Age\":714.0,\"_all\":891.0}}";
    assertEquals(expected, actual);
  }

  @Test
  public void AggregateSum() throws IOException {
    String actual = sendGetRequest("{\n  \"_sum\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic1", "Aggregate");
    String expected = "{\"_sum\":{\"PassengerId\":397386.0,\"Age\":21205.17}}";
    assertEquals(expected, actual);
  }

  @Test
  public void AggregateAvg() throws IOException {
    String actual = sendGetRequest("{\n  \"_avg\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic1", "Aggregate");
    String expected = "{\"_avg\":{\"PassengerId\":446.0,\"Age\":23.8}}";
    assertEquals(expected, actual);
  }

  @Test
  public void AggregateMax() throws IOException {
    String actual = sendGetRequest("{\n  \"_max\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic1", "Aggregate");
    String expected = "{\"_max\":{\"PassengerId\":891.0,\"Age\":80.0}}";
    assertEquals(expected, actual);
  }

  @Test
  public void AggregateMin() throws IOException {
    String actual = sendGetRequest("{\n  \"_min\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic1", "Aggregate");
    String expected = "{\"_min\":{\"PassengerId\":0.0,\"Age\":0.0}}";
    assertEquals(expected, actual);
  }
}