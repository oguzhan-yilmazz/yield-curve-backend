package com.project.yieldcurve.service;

import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondInstrument;


@Service
public class InterestCalculatorService {
	
	private final ValidationService validationService;
	private final DataCalculatorService dataCalculatorService;
	
	// constructor injection
	public InterestCalculatorService(ValidationService validationService, DataCalculatorService dataCalculatorService ) {
		this.validationService= validationService;
		this.dataCalculatorService = dataCalculatorService;
	}
	
	// birikmiş faiz tutarının hesaplayan fonksiyon
	public double calculateAccruedInterest(BondInstrument bond) {
		try {
			validationService.validateBond(bond);
			double totalDays = ChronoUnit.DAYS.between(bond.getStartDate(), bond.getEndDate());
			long elapsedDays = ChronoUnit.DAYS.between(bond.getStartDate(), bond.getBusinessDate());
			
			if (bond.getBusinessDate().isAfter(bond.getStartDate())) {
				elapsedDays -= ChronoUnit.DAYS.between(bond.getStartDate(), bond.getBusinessDate());
			}
			
			// Faiz ödeme frekansını belirle
			int paymentFrequency = (dataCalculatorService.yearsToMaturity(bond) >= 0.5) ? 2 : 1;

			double couponPayment = (bond.getCouponRate() / 100) * bond.getNominalValue() / paymentFrequency; 

			return (elapsedDays / totalDays) * couponPayment;
		} catch (Exception e) {
			System.err.println("Bir hata oluştu: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}


}
