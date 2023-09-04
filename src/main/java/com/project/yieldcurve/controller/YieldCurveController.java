package com.project.yieldcurve.controller;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.yieldcurve.service.YieldCurveService;

@RestController
@RequestMapping("/api/yieldcurve")
public class YieldCurveController {

    @Autowired
    private YieldCurveService yieldCurveService;
    
    
    // CrossOrigin ile sadece belirli domainlerden gelen isteklere izin veriyoruz, bu bir güvenlik önlemi.
    @CrossOrigin(origins = {"http://localhost:3000", "https://yieldcurve.netlify.app"})
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, List<Double>>> calculateYieldCurve() throws Exception  {
        Map<String, List<Double>> result = yieldCurveService.getYieldCurveData();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    
    @CrossOrigin(origins = {"http://localhost:3000", "https://yieldcurve.netlify.app"})
    @GetMapping("/calculateMaturity")
    public ResponseEntity<Map<String, List<LocalDate>>>  calculateYieldCurveMaturity() throws Exception {
    	Map<String, List<LocalDate>> result = yieldCurveService.getMaturityDates();
    	return new ResponseEntity<>(result, HttpStatus.OK);
    	
    }
    @CrossOrigin(origins = {"http://localhost:3000", "https://yieldcurve.netlify.app"})
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestBody String fileContent) {
        // Dosya içeriğini YieldCurveService'e gönder
        yieldCurveService.calculateYieldCurve(fileContent);
        System.out.println("Received file content: " + fileContent);
        // yieldCurveService.calculateYieldCurve(fileContent);
        return ResponseEntity.ok("File content received");
    }
 
}
