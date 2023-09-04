package com.project.yieldcurve.service;

import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondInstrument;

@Service
public class ValidationService {
	
	
	public void validateBond(BondInstrument bond) {
		if (bond == null || bond.getStartDate() == null || bond.getEndDate() == null || bond.getBusinessDate() == null) {
			throw new IllegalArgumentException("Bono veya tarih bilgisi eksik.");
		}
		double totalDays = ChronoUnit.DAYS.between(bond.getStartDate(), bond.getEndDate());
		if (totalDays == 0) {
			throw new ArithmeticException("Toplam gün sayısı 0 olamaz.");
		}
	}

}
