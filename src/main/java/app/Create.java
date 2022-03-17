package app;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.ByteBuffer;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/api/create")
public class Create {

    public List<Map<String, ByteBuffer>> parseCSV(InputPart inputPart) throws IOException{
        long start2 = System.currentTimeMillis();

        List<Map<String, ByteBuffer>> res = new ArrayList<Map<String, ByteBuffer>>();
        ExecutorService pool = Executors.newCachedThreadPool();
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        String line = buffer.readLine();
        line = line.replaceAll("\\r", "");
        String[] headers = line.split(",");
        System.out.println(line);
        for(String header : headers){
            App.headers.add(header);
        }
        while ((line = buffer.readLine()) != null) {
            final String lineFinal = line;
            pool.submit(() -> {
                Map<String, ByteBuffer> row = new HashMap<String, ByteBuffer>();
                String[] values = lineFinal.split(",");
                for(int j = 0; j<headers.length-1; j++){
                    ByteBuffer value = ByteBuffer.wrap(values[j].getBytes());
                    row.put(headers[j], value);
                }
                res.add(row);
            });
        }
        long end2 = System.currentTimeMillis();      
        System.out.println("Elapsed Time in milli seconds: "+ (end2-start2));
        return res;
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
        return Response.ok("Bien reçu").build();
    }
}
