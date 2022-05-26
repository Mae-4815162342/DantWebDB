package integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.awt.Desktop;  

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
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
  private static String baseURI = "http://localhost:8080/api";
  
  @Before
  public void setUp(){
    this.client = new ResteasyClientBuilder().build();
    this.client.register(GsonProvider.class);
  }

  @After
  public void teardown(){
    this.client.close();
  }

  @Test
  public void t1_CreationTable() {
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(baseURI));
        String input = "{\n  \"tableName\": \"titanic\",\n  \"columns\":{\n    \"PassengerId\": \"string\",\n    \"Survived\": \"string\",\n        \"Pclass\": \"string\",\n        \"Name\": \"string\",\n        \"Sex\": \"string\",\n        \"Age\": \"int\",\n        \"SibSp\": \"string\",\n        \"Parch\": \"string\",\n        \"Ticket\": \"string\",\n        \"Fare\": \"string\",\n        \"Cabin\": \"string\",\n        \"Embarked\": \"string\"\n  }\n}";
        System.out.println("• Sending post request to localhost");

        Response response = target
                .path("/table-json")
                .queryParam("fromClient", true)
                .request()
                .post(Entity.entity(input, MediaType.APPLICATION_JSON));
        String actual = response.readEntity(String.class);
        String expected = "Table created:titanic \n With columns : {PassengerId=string, Survived=string, Pclass=string, Name=string, Sex=string, Age=int, SibSp=string, Parch=string, Ticket=string, Fare=string, Cabin=string, Embarked=string}";
        assertEquals(expected, actual);
        response.close();
  }
  @Test
  public void t2_DuplicateTable() {
    ResteasyWebTarget target = client.target(UriBuilder.fromPath(baseURI));
    String input = "{\n  \"tableName\": \"titanic\",\n  \"columns\":{\n    \"PassengerId\": \"string\",\n    \"Survived\": \"string\",\n        \"Pclass\": \"string\",\n        \"Name\": \"string\",\n        \"Sex\": \"string\",\n        \"Age\": \"int\",\n        \"SibSp\": \"string\",\n        \"Parch\": \"string\",\n        \"Ticket\": \"string\",\n        \"Fare\": \"string\",\n        \"Cabin\": \"string\",\n        \"Embarked\": \"string\"\n  }\n}";
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
  public void t3_InsertFile(){
    ResteasyWebTarget target = client.target(UriBuilder.fromPath(baseURI));
    MultipartFormDataOutput form = new MultipartFormDataOutput();
    File csv = new File("/Users/diez/Documents/Fac/DantWebDB/titanic.csv");
    
    form.addFormData("tableName", "titanic", MediaType.TEXT_PLAIN_TYPE);
    form.addFormData("file", csv , MediaType.APPLICATION_OCTET_STREAM_TYPE);
    Response response = target
      .path("/upload")
      .request()
      .post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
    String actual = response.readEntity(String.class);
    String expected = "Values from file inserted into titanic!";
    System.out.println(actual);
    assertEquals(expected, actual);
    response.close();
  }
}