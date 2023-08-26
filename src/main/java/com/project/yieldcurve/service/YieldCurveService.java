package com.project.yieldcurve.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondCalculator;
import com.project.yieldcurve.BondInstrument;
import com.project.yieldcurve.DataMapper;
import com.project.yieldcurve.JsonParser;
import com.project.yieldcurve.SimpleInjector;
import com.project.yieldcurve.YieldCurveCalculator;

@Service
public class YieldCurveService {

    public Map<String, List<Double>> calculateYieldCurve() throws Exception {
        // Main.java'dan taşıdığınız kodlar burada
        // ...
		
        JsonParser jsonParser = SimpleInjector.getJsonParser();
        DataMapper dataMapper = SimpleInjector.getDataMapper();
		
		//JsonParser readJson = new JsonParser();
		//DataMapper bondlist = new DataMapper();
		
	
		String filePath = "src/main/resources/bondData.json";
		
		JSONObject dataJson = jsonParser.parseFromFile(filePath);
		
		
		List<BondInstrument> bondlists = dataMapper.mapToBondInstrumentList(dataJson);
		//System.out.println(bondlists);
		YieldCurveCalculator yieldCurveCalculator = new YieldCurveCalculator();
		BondCalculator bondcalculator = new BondCalculator(yieldCurveCalculator);
		System.out.println(bondcalculator.buildYieldCurveDataMap(bondlists));
        List<Double> maturities = bondcalculator.getMaturities();
        List<Double> yields = bondcalculator.getYields();
        List<LocalDate> maturityDates = bondcalculator.getMaturityDates();
        //System.out.println("maturities: "+ maturities);
        //System.out.println("yields: " + yields);
        Map<String, List<Double>> result = new HashMap<>();
        result.put("maturities", maturities);
        result.put("yields", yields);
        return result;
    }
}
