package app;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import filter.GsonProvider;

import javax.ws.rs.core.MediaType;


import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("/api/table")
public class Table {
    @GET

    public Response get() {
      return Response.ok(App.data).build();
    }
}
