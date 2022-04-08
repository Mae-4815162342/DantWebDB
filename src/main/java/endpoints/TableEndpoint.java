package endpoints;

import controller.Worker;
import exception.TableExistsException;
import model.Table;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TableEndpoint {

    @POST
    @Path("/table-json")
    public Response createTableFromJson(Table input) {
        /* récupération des informations de la table */
        final String TABLE_NAME = input.getTableName();
        final HashMap<String, String> COLUMNS =  input.getColumns();

        try {
            /* ajout dans la database */
            Worker.getInstance();
            Worker.createTable(TABLE_NAME,COLUMNS);
            return Response.ok("Table created:" + TABLE_NAME + " \n With columns : " + COLUMNS).build();

        } catch(TableExistsException e) {
            return Response.status(400).entity(e.getMessage()).type("plain/text").build();
        }
    }
}
