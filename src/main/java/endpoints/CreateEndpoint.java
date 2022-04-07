/*
package endpoints;

import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/api/create")
public class CreateEndpoint {
    final int CHUNK_SIZE = 100_000; 
    public int  sumSizeBuffer(List<ByteArrayOutputStream> buffers){
        int res = 0;
        for(ByteArrayOutputStream buffer : buffers){
            res += buffer.size();
        }
        return res;
    }
    public void sendChunks(List<ByteArrayOutputStream> buffers){
        //envoi de requête
        System.out.println("ON VIDE");
        for(ByteArrayOutputStream buffer : buffers){
            buffer.reset();
        }
    }
    public byte[] parseCSV(InputPart inputPart) throws IOException{
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        List<ByteArrayOutputStream> buffers = new ArrayList<ByteArrayOutputStream>();
        for(int j = 0; j<5; j++){
            buffers.add(new ByteArrayOutputStream());
        }
        String line = buffer.readLine();
        line = line.replaceAll("\\r", "");
        String[] headers = line.split(",");
        for(String header : headers){
            App.headers.add(header);
        }
        int i = 0;
        byte[] res = {};
        while((line = buffer.readLine()) != null){
            if(sumSizeBuffer(buffers) > 500_000_000){
                sendChunks(buffers);
            }
            if(res.length + line.getBytes().length < CHUNK_SIZE){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(res);
                outputStream.write((line + "\n").getBytes());
                res = outputStream.toByteArray();
            }
            else{
                byte[] newRes = {};
                buffers.get(i % 5).write(res);
                res = newRes;
                i++;
            }
        } 
        sendChunks(buffers);
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
*/
