package com.project.yieldcurve.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondInstrument;


@Service
public class DataCalculatorService {
	
	final static int DAYS_PER_YEAR = 365; // Bir yıldaki gün sayısı
	
	private final ValidationService validationService;
	private final BondYieldCalculatorService bondYieldCalculatorService;
	
	public DataCalculatorService(ValidationService validationService, BondYieldCalculatorService bondYieldCalculatorService) {
		this.validationService= validationService;
		this.bondYieldCalculatorService = bondYieldCalculatorService;
	}

	/**
	 * Bonoların vadesine kalan gün ve yıl sayısını hesaplar ve ekrana yazdırır.
	 *
	 * @param bondList Vadesine kalan zamanı hesaplanacak bono listesi
	 */
	public void calculateDaysToMaturity(List<BondInstrument> bondList) {
		if (bondList == null) {
			System.err.println("Bonolara dair bir liste bulunamadı.");
			return;
		}
		try {
			for (BondInstrument bond : bondList) {
				if (bond == null || bond.getBusinessDate() == null || bond.getEndDate() == null) {
					System.err.println("Bir bono veya tarih bilgisi eksik.");
					continue; // Geçerli iterasyonu atlayıp sonraki bonoya geçer
				}
				// İlgili bononun işlem tarihi ile vade sonu tarihi arasındaki gün sayısını
				// hesaplar
				long daysToMaturity = ChronoUnit.DAYS.between(bond.getBusinessDate(), bond.getEndDate());

				// Gün sayısını 365'e böler, bu da yıl cinsinden vadesine kalan zamanı verir
				double yearsToMaturity = daysToMaturity / 365.0;

				System.out.println("Series ID: " + bond.getSeriesId() + " - Days to Maturity: " + daysToMaturity
						+ " - Years to Maturity: " + yearsToMaturity);
			}
		} catch (Exception e) {
			System.err.println("Bir hata oluştu: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public double yearsToMaturity(BondInstrument bond) {
		try {
			// İlgili bononun işlem tarihi ile vade sonu tarihi arasındaki gün sayısını
			// hesaplar
			validationService.validateBond(bond);

			long daysToMaturity = ChronoUnit.DAYS.between(bond.getBusinessDate(), bond.getEndDate());

			// Gün sayısını 365'e böler, bu da yıl cinsinden vadesine kalan zamanı verir
			double yearsToMaturityValue = daysToMaturity / 365.0;
			yearsToMaturityValue = bondYieldCalculatorService.formatToDecimalPlaces(yearsToMaturityValue, 4);
			return yearsToMaturityValue;

		} catch (Exception e) {
			System.err.println("Bir hata oluştu: " + e.getMessage());
			e.printStackTrace();
			return 0; // Hata durumunda dönülecek varsayılan değer
		}
	}
	

	// bu fonksiyon islem tarihine tahvilin vadesine kalan günü ekler böylece yield noktalarındaki tarihi döndürür. 
	public LocalDate findMaturityDate(LocalDate businessDate, Double maturity) {
	    final int DAYS_PER_YEAR = 365;
	    LocalDate date = businessDate;
	    int add_day = (int) Math.round(maturity * DAYS_PER_YEAR);
	    date = date.plus(add_day, ChronoUnit.DAYS);
	    return date;
	}

	// Vade tarihinden geriye doğru ilerleyerek iş günü tarihini hesaplar
	public LocalDate findBusinessDate(LocalDate maturityDate, double remainingYears) {

		long daysToSubtract = (long) (remainingYears * DAYS_PER_YEAR); // Çıkarılması gereken gün sayısı
		LocalDate businessDate = maturityDate.minus(daysToSubtract, ChronoUnit.DAYS);
		return businessDate;
	}
	
	

}
