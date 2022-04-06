package app;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/api/create")
public class Create {
    public List<Map<String, String>> parseCSV(InputPart inputPart) throws IOException{
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String fic = new String(bytes);
        fic = fic.replaceAll("\\r", "");
        String[] lines = fic.split("\n");
        String[] headers = lines[0].split(",");
        System.out.println(lines[0]);
        for(String header : headers){
            App.headers.add(header);
        }
        for(int i = 1; i<lines.length -1; i++){
            Map<String, String> line = new HashMap<String, String>();
            String[] values = lines[i].split(",");
            for(int j = 0; j<headers.length-1; j++){
                line.put(headers[j], values[j]);
            }
            res.add(line);
        }
        return res;
    }
    @POST
    @Consumes("multipart/form-data")
    public Response post(MultipartFormDataInput input) throws IOException {
        final String UPLOADED_FILE_PARAMETER_NAME = "file";
        
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);
        for (InputPart inputPart : inputParts) {
            App.data = parseCSV(inputPart);
        }
        return Response.ok(App.data).build();
    }
}
