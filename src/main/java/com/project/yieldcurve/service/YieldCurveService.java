package com.project.yieldcurve.service;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondCalculator;
import com.project.yieldcurve.BondInstrument;
import com.project.yieldcurve.DataMapper;
import com.project.yieldcurve.JsonParser;
import com.project.yieldcurve.YieldCurveCalculator;

@Service
public class YieldCurveService {

    private static final Logger logger = LoggerFactory.getLogger(YieldCurveService.class);
	private static final String BOND_DATA_PATH = "src/main/resources/bondData.json";
	
	Map<String, List<Double>> yieldCurveData = new HashMap<>();
	Map<String, List<LocalDate>> maturityDate = new HashMap<>();

	private JsonParser jsonParser;
	private DataMapper dataMapper;
	private BondCalculator bondCalculator;

	@Autowired
	public YieldCurveService(JsonParser jsonParser, DataMapper dataMapper,

			BondCalculator bondCalculator) {
		this.jsonParser = jsonParser;
		this.dataMapper = dataMapper;
		this.bondCalculator = bondCalculator;
	}

	public void calculateYieldCurve() {
		
		try {

			JSONObject dataJson = jsonParser.parseFromFile(BOND_DATA_PATH);
			if (dataJson == null) {
				throw new Exception("Data JSON is null");
			}
		
			// web: java -jar target/yieldcurve-0.0.1-SNAPSHOT.jar
			// web: java -Dserver.port=$PORT $JAVA_OPTS -jar
			// target/yieldcurve-0.0.1-SNAPSHOT.jar

			List<BondInstrument> bondLists = dataMapper.mapToBondInstrumentList(dataJson);
			
			updateMaps(bondLists);
		}
		 catch (IOException e) {
		        logger.error("Failed to read the bond data or map JSON: " + e.getMessage());
		    } catch (Exception e) {
		        logger.error("An unexpected error occurred: " + e.getMessage());
		    }
	}
	
    private void updateMaps(List<BondInstrument> bondLists) {
    	bondCalculator.buildYieldCurveDataMap(bondLists);
        List<Double> maturitiess = bondCalculator.getMaturities();
        List<Double> yieldss = bondCalculator.getYields();
        List<LocalDate> maturityDatess = bondCalculator.getMaturityDates();

        yieldCurveData.put("maturities", maturitiess);
        yieldCurveData.put("yields", yieldss);
        maturityDate.put("maturityDates", maturityDatess);
    }

	public Map<String, List<LocalDate>> getMaturityDates() {
		return maturityDate;
	}

	public Map<String, List<Double>> getYieldCurveData(){
		return yieldCurveData;
	}

}
