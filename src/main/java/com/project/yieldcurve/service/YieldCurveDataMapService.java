package com.project.yieldcurve.service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondInstrument;



@Service
public class YieldCurveDataMapService {

	final static int DAYS_PER_YEAR = 365; // Bir yıldaki gün sayısı
	//private final YieldCurveCalculator yieldCurveCalculator;
	private final BondYieldCalculatorService bondYieldCalculatorService;
	private final DataCalculatorService dataCalculatorService;
	private final ValidationService validationService;

	private List<Double> maturities = new ArrayList<Double>();// Vadeye kalan süreleri ( yıl cinsinden) tutan liste
	private List<Double> yields = new ArrayList<Double>(); // Yield değerleri
	private List<LocalDate> maturityDates = new ArrayList<LocalDate>(); // Vade tarihlerini tutacak liste ( tarih
																		// cinsinden)

	// // constructor injection
	public YieldCurveDataMapService (BondYieldCalculatorService bondYieldCalculatorService, DataCalculatorService dataCalculatorService, ValidationService validationService ){
		this.bondYieldCalculatorService = bondYieldCalculatorService;
		this.dataCalculatorService = dataCalculatorService;
		this.validationService= validationService;
	}

	public List<Double> getMaturities() {
		return maturities;
	}

	public List<Double> getYields() {
		return yields;
	}

	public List<LocalDate> getMaturityDates() {
		return maturityDates;
	}

	public Map<Double, Double> buildYieldCurveDataMap(List<BondInstrument> bonds) {
		
	    maturities.clear();
	    yields.clear();
	    maturityDates.clear();

		if (bonds == null || bonds.isEmpty()) {
			throw new IllegalArgumentException("Bono listesi boş veya null olamaz.");
		}

		Map<Double, Double> yieldCurveDataMap = new TreeMap<>();

		for (BondInstrument bond : bonds) {
			validationService.validateBond(bond); // Bono bilgisinin geçerli olup olmadığını kontrol eder

			double rawMaturityInYears = bondYieldCalculatorService.formatToDecimalPlaces(dataCalculatorService.yearsToMaturity(bond), 4);// Vade
																												// yıllarını
																												// hesaplar
			double yieldToMaturity = bondYieldCalculatorService.calculateYieldToMaturity(rawMaturityInYears, bond);
			LocalDate maturityDate = dataCalculatorService.findMaturityDate(bond.getBusinessDate(), rawMaturityInYears);

			// İsteğe bağlı olarak verileri loglamak
			System.out.println("Maturity: " + rawMaturityInYears + ", YTM: " + yieldToMaturity);

			maturityDates.add(maturityDate);
			maturities.add(rawMaturityInYears);
			yields.add(yieldToMaturity);
			yieldCurveDataMap.put(rawMaturityInYears, yieldToMaturity);
		}
		
		System.out.println("yieldCurveDataMap: "+ yieldCurveDataMap);

		return yieldCurveDataMap;
	}

}
