package com.project.yieldcurve;
import org.json.JSONObject;
import org.springframework.stereotype.Component;// Spring anotasyonu
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Component  
public class JsonParser {
//
     // // Reads JSON content from the specified file path and returns it as a JSONObject.
 
    public JSONObject parseFromFile(String filePath) throws Exception {
    	
        // Check if filePath is null or empty
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }
        
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        
        // Check if the content read is null or empty
        if (content == null || content.isEmpty()) {
            throw new IOException("File content is empty or null.");
        }

        return new JSONObject(content);
    }
}
