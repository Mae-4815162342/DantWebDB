package integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import filter.GsonProvider;

public class SelectFindTest {
  private ResteasyClient client;
  private final String baseURI = "http://localhost:8081/api";
  private ResteasyWebTarget target;
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public String formatJSON(String jsonString){
    JsonElement el = JsonParser.parseString(jsonString);
    return gson.toJson(el);
  }
  @Before
  public void setUp() {
    this.client = new ResteasyClientBuilder().build();
    this.client.register(GsonProvider.class);
    this.target = client.target(UriBuilder.fromPath(baseURI));
    String input = "{\n  \"tableName\": \"titanic2\",\n  \"columns\":{\n    \"PassengerId\": \"int\",\n    \"Survived\": \"string\",\n        \"Pclass\": \"string\",\n        \"Name\": \"string\",\n        \"Sex\": \"string\",\n        \"Age\": \"int\",\n        \"SibSp\": \"string\",\n        \"Parch\": \"string\",\n        \"Ticket\": \"string\",\n        \"Fare\": \"string\",\n        \"Cabin\": \"string\",\n        \"Embarked\": \"string\"\n  }\n}";
    System.out.println("• Sending post request to localhost");
  
    Response response = target
          .path("/table-json")
          .queryParam("fromClient", true)
          .request()
          .post(Entity.entity(input, MediaType.APPLICATION_JSON));
    if(!response.readEntity(String.class).equals("titanic2 already exists in the database !")){
      MultipartFormDataOutput form = new MultipartFormDataOutput();
      File csv = new File(FilesPath.TITANIC_PATH);
      form.addFormData("tableName", "titanic2", MediaType.TEXT_PLAIN_TYPE);
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
  public void FindIn() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"where\":{\"Age\":{\"in\":[80]}}}"), "titanic2", "findMany");
    String expected = "[{\"Embarked\":\"S\",\"Survived\":\"1\",\"Pclass\":\"1\",\"Ticket\":\"27042\",\"PassengerId\":\"631\",\"Parch\":\"0\",\"Cabin\":\"A23\",\"Sex\":\"male\",\"SibSp\":\"0\",\"Age\":\"80\",\"Name\":\"Barkworth, Mr. Algernon Henry Wilson\",\"Fare\":\"30\"}]";
    assertEquals(expected, actual);
  }
  @Test
  public void FindNotIn() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"select\":[\"PassengerId\"],\"where\":{\"Name\":{\"notIn\":[\"Braund, Mr. Owen Harris\"]}},\"limit\":1}"), "titanic2", "findMany");
    String expected = "[{\"PassengerId\":\"2\"}]";
    assertEquals(expected, actual);
  }
  @Test
  public void FindGreaterEquals() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"where\":{\"Age\":{\"gte\":80}}}"), "titanic2", "findMany");
    String expected = "[{\"Embarked\":\"S\",\"Survived\":\"1\",\"Pclass\":\"1\",\"Ticket\":\"27042\",\"PassengerId\":\"631\",\"Parch\":\"0\",\"Cabin\":\"A23\",\"Sex\":\"male\",\"SibSp\":\"0\",\"Age\":\"80\",\"Name\":\"Barkworth, Mr. Algernon Henry Wilson\",\"Fare\":\"30\"}]";
    assertEquals(expected, actual);
  }
  
  @Test
  public void FindGreater() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"where\":{\"Age\":{\"gt\":80}}}"), "titanic2", "findMany");
    String expected = "[]";
    assertEquals(expected, actual);
  }
  
  @Test
  public void FindLeast() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"where\":{\"Age\":{\"lt\":0.0}}}"), "titanic2", "findMany");
    String expected = "[]";
    assertEquals(expected, actual);
  }
  @Test

  public void FindLeastEquals() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"where\":{\"Age\":{\"lt\":0.5}}}"), "titanic2", "findMany");
    String expected = "[{\"Embarked\":\"C\",\"Survived\":\"1\",\"Pclass\":\"3\",\"Ticket\":\"2625\",\"PassengerId\":\"804\",\"Parch\":\"1\",\"Cabin\":\"\",\"Sex\":\"male\",\"SibSp\":\"0\",\"Age\":\"0.42\",\"Name\":\"Thomas, Master. Assad Alexander\",\"Fare\":\"8.5167\"}]";
    assertEquals(expected, actual);
  }
  @Test
  public void FindContains() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"select\":[\"PassengerId\"],\"where\":{\"Name\":{\"contains\":\"Thomas\"},\"PassengerId\":{\"gt\":800}}}"), "titanic2", "findMany");
    String expected = "[{\"PassengerId\":\"804\"},{\"PassengerId\":\"807\"},{\"PassengerId\":\"829\"},{\"PassengerId\":\"842\"},{\"PassengerId\":\"880\"}]";
    assertEquals(expected, actual);
  }
  @Test
  public void FindStartsWith() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"select\":[\"PassengerId\"],\"where\":{\"Name\":{\"startsWith\":\"Thomas\"}}}"), "titanic2", "findMany");
    String expected = "[{\"PassengerId\":\"804\"}]";
    assertEquals(expected, actual);
  }
  @Test
  public void FindEndsWith() throws IOException {
    String actual = sendGetRequest(formatJSON("{\"select\":[\"PassengerId\"],\"where\":{\"Name\":{\"endsWith\":\"Alexander\"},\"PassengerId\":{\"gt\":800,\"lt\":815}}}"), "titanic2", "findMany");
    String expected = "[{\"PassengerId\":\"804\"}]";
    assertEquals(expected, actual);
  }
}