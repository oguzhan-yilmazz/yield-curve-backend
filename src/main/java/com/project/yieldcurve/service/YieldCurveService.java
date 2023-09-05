package com.project.yieldcurve.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.project.yieldcurve.BondInstrument;
import com.project.yieldcurve.DataMapper;
import com.project.yieldcurve.JsonParser;


@Service
public class YieldCurveService {

 
	private static final String BOND_DATA_PATH = "src/main/resources/bondData.json";
	
	Map<String, List<Double>> yieldCurveData = new HashMap<>();
	Map<String, List<LocalDate>> maturityDate = new HashMap<>();
	Map<String,LocalDate> businessDate = new HashMap<>();
	

	private JsonParser jsonParser;
	private DataMapper dataMapper;
	private final YieldCurveDataMapService yieldCurveDataMapService;

	
	public YieldCurveService(JsonParser jsonParser, DataMapper dataMapper,

			YieldCurveDataMapService yieldCurveDataMapService) {
		this.jsonParser = jsonParser;
		this.dataMapper = dataMapper;
		this.yieldCurveDataMapService= yieldCurveDataMapService;
	
	}

	
	// apiye verilerin taşındıgı kısım
	public void calculateYieldCurve(String jsonContent) {
		
		try {

	        JSONObject dataJson;
	        
	        // Eğer jsonContent null veya boş ise, dosyadan veriyi oku
	        if (jsonContent == null || jsonContent.isEmpty()) {
	            dataJson = jsonParser.parseFromFile(BOND_DATA_PATH);
	        } else { // Değilse, doğrudan sağlanan JSON içeriğini kullan
	            dataJson = new JSONObject(jsonContent);
	            
	        }
	        
	        if (dataJson == null) {
	            throw new Exception("Data JSON is null");
	        }
			
			List<BondInstrument> bondLists = dataMapper.mapToBondInstrumentList(dataJson);
			System.out.println("bondlist :"+ bondLists);
		
			
			updateMaps(bondLists);
	
		}
		 catch (IOException e) {
			 System.err.println("Failed to read the bond data or map JSON: " + e.getMessage());
		    } catch (Exception e) {
		    	System.err.println("An unexpected error occurred: " + e.getMessage());
		    }
	}
	// yardımcı fonksiyon
    private void updateMaps(List<BondInstrument> bondLists) {
        // Mevcut verileri temizle
        yieldCurveData.clear();
        maturityDate.clear();
    	
    	yieldCurveDataMapService.buildYieldCurveDataMap(bondLists);
    	
        List<Double> maturitiess = yieldCurveDataMapService.getMaturities();
     
        List<Double> yieldss = yieldCurveDataMapService.getYields();
      
        List<LocalDate> maturityDatess = yieldCurveDataMapService.getMaturityDates();
        
        LocalDate businessDatee =  bondLists.get(0).getBusinessDate();
        
   
   
        yieldCurveData.put("maturities", maturitiess);
     
        yieldCurveData.put("yields", yieldss);
       
        maturityDate.put("maturityDates", maturityDatess);
        
        businessDate.put("businessDate", businessDatee);

 
    }

    
	public Map<String, List<LocalDate>> getMaturityDates() {
		return maturityDate;
	}
	

	public Map<String, List<Double>> getYieldCurveData(){
		return yieldCurveData;
	}
	
	public Map<String, LocalDate> getBusinessDate(){
		return businessDate;
	}

}
