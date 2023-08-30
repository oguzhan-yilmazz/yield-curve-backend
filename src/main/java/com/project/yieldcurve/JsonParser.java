package com.project.yieldcurve;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;// Spring anotasyonu
import java.nio.file.Files;
import java.nio.file.Paths;


@Component  
public class JsonParser {
//
     // Belirtilen dosya yolu üzerinden JSON içeriğini okuyarak JSONObject olarak döndürür.
 
    public JSONObject parseFromFile(String filePath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(content);
    }
}
