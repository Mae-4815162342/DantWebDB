package app;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/api/create")
public class Create {
    final int CHUNK_SIZE = 100_000; 
    public byte[] parseCSV(InputPart inputPart) throws IOException{
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        
        String line = buffer.readLine();
        line = line.replaceAll("\\r", "");
        String[] headers = line.split(",");
        for(String header : headers){
            App.headers.add(header);
        }
        int i = 0;
        byte[] res = {};
        while((line = buffer.readLine()) != null){
            if(res.length + line.getBytes().length < CHUNK_SIZE){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(res);
                outputStream.write((line + "\n").getBytes());
                res = outputStream.toByteArray();
            }
            else{
                byte[] newRes = {};
                // Copie du chunk dans une variable de type final
                res = newRes;
                // Thread pour envoyer la requête...
                i++;
            }
        } 
        if(buffer.readLine() == null){
            System.out.println("Tout lu !");
        } 
        System.out.println("Nombre de chunks crées : " + i);
        return null;
    } 
    @POST
    @GZIP
    @Consumes("multipart/form-data")
    public Response post(@GZIP MultipartFormDataInput input) throws IOException {
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        
        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            App.data = parseCSV(inputPart);
        }
        if(App.data != null){
            System.out.println("Stocké !");
            System.out.println((long) App.data.length);
        }
        return Response.ok("Bien reçu").build();
    }
}
