package com.project.yieldcurve;

import com.project.yieldcurve.JsonParser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import com.project.yieldcurve.BondCalculator;

import org.json.JSONObject;
//import yieldCurve.YieldCurveGraph;
public class Main {

	public static void main(String[] args) throws Exception {
			    
		
        JsonParser jsonParser = SimpleInjector.getJsonParser();
        DataMapper dataMapper = SimpleInjector.getDataMapper();
		
		//JsonParser readJson = new JsonParser();
		//DataMapper bondlist = new DataMapper();
        //web: java -jar target/yieldcurve-0.0.1-SNAPSHOT.jar
	
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
        System.out.println("maturities: "+ maturities);
        System.out.println("yields: " + yields);
        YieldCurveGraph.plotYieldCurve(maturities, yields, maturityDates);					
	    }				
}