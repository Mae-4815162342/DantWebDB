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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import filter.GsonProvider;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Scenario1 {
  private ResteasyClient client;
  private final String baseURI = "http://localhost:8080/api";
  private ResteasyWebTarget target;

  @Before
  public void setUp() {
    this.client = new ResteasyClientBuilder().build();
    this.client.register(GsonProvider.class);
    this.target = client.target(UriBuilder.fromPath(baseURI));
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
  public void t1_CreationTable() {
    ResteasyWebTarget target = client.target(UriBuilder.fromPath(baseURI));
    String input = "{\n  \"tableName\": \"titanic\",\n  \"columns\":{\n    \"PassengerId\": \"int\",\n    \"Survived\": \"string\",\n        \"Pclass\": \"string\",\n        \"Name\": \"string\",\n        \"Sex\": \"string\",\n        \"Age\": \"int\",\n        \"SibSp\": \"string\",\n        \"Parch\": \"string\",\n        \"Ticket\": \"string\",\n        \"Fare\": \"string\",\n        \"Cabin\": \"string\",\n        \"Embarked\": \"string\"\n  }\n}";
    System.out.println("• Sending post request to localhost");

    Response response = target
        .path("/table-json")
        .queryParam("fromClient", true)
        .request()
        .post(Entity.entity(input, MediaType.APPLICATION_JSON));
    String actual = response.readEntity(String.class);
    String expected = "Table created:titanic \n With columns : {PassengerId=int, Survived=string, Pclass=string, Name=string, Sex=string, Age=int, SibSp=string, Parch=string, Ticket=string, Fare=string, Cabin=string, Embarked=string}";
    assertEquals(expected, actual);
    response.close();
  }

  @Test
  public void t2_DuplicateTable() {
    ResteasyWebTarget target = client.target(UriBuilder.fromPath(baseURI));
    String input = "{\n  \"tableName\": \"titanic\",\n  \"columns\":{\n    \"PassengerId\": \"int\",\n    \"Survived\": \"int\",\n        \"Pclass\": \"string\",\n        \"Name\": \"string\",\n        \"Sex\": \"string\",\n        \"Age\": \"int\",\n        \"SibSp\": \"string\",\n        \"Parch\": \"string\",\n        \"Ticket\": \"string\",\n        \"Fare\": \"string\",\n        \"Cabin\": \"string\",\n        \"Embarked\": \"string\"\n  }\n}";
    System.out.println("• Sending post request to localhost");

    Response response = target
        .path("/table-json")
        .queryParam("fromClient", true)
        .request()
        .post(Entity.entity(input, MediaType.APPLICATION_JSON));
    String actual = response.readEntity(String.class);
    String expected = "titanic already exists in the database ! already exists in the database !";
    assertEquals(expected, actual);
    response.close();
  }

  @Test
  public void t3_InsertFile() throws IOException {
    MultipartFormDataOutput form = new MultipartFormDataOutput();
    File csv = new File("/Users/diez/Documents/Fac/DantWebDB/titanic.csv");
    form.addFormData("tableName", "titanic", MediaType.TEXT_PLAIN_TYPE);
    form.addFormData("file", csv, MediaType.APPLICATION_OCTET_STREAM_TYPE);
    Response response = target
        .path("/upload")
        .request()
        .post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
    String actual = response.readEntity(String.class);
    String expected = "Values from file inserted into titanic!";
    assertEquals(expected, actual);
    response.close();
  }

  @Test
  public void t4_AggregateCount() throws IOException {
    String actual = sendGetRequest("{\n  \"_count\": [\n    \"Cabin\", \"Age\", \"_all\"\n  ]\n}", "titanic",
        "Aggregate");
    String expected = "{\"_count\":{\"Cabin\":204.0,\"Age\":713.0,\"_all\":890.0}}";
    assertEquals(expected, actual);
  }

  @Test
  public void t5_AggregateSum() throws IOException {
    String actual = sendGetRequest("{\n  \"_sum\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic", "Aggregate");
    String expected = "{\"_sum\":{\"PassengerId\":397385.0,\"Age\":21183.17}}";
    assertEquals(expected, actual);
  }

  @Test
  public void t6_AggregateAvg() throws IOException {
    String actual = sendGetRequest("{\n  \"_avg\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic", "Aggregate");
    String expected = "{\"_avg\":{\"PassengerId\":446.5,\"Age\":23.8}}";
    assertEquals(expected, actual);
  }

  @Test
  public void t7_AggregateMax() throws IOException {
    String actual = sendGetRequest("{\n  \"_max\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic", "Aggregate");
    String expected = "{\"_max\":{\"PassengerId\":891.0,\"Age\":80.0}}";
    assertEquals(expected, actual);
  }

  @Test
  public void t8_AggregateMin() throws IOException {
    String actual = sendGetRequest("{\n  \"_min\": [\n    \"Age\", \"PassengerId\"\n  ]\n}", "titanic", "Aggregate");
    String expected = "{\"_min\":{\"PassengerId\":0.0,\"Age\":0.0}}";
    assertEquals(expected, actual);
  }
}