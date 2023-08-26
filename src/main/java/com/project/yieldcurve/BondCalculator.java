package com.project.yieldcurve;

import com.project.yieldcurve.YieldCurveCalculator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.project.yieldcurve.YieldCurveCalculator;

public class BondCalculator {
	final int DAYS_PER_YEAR = 365; // Bir yıldaki gün sayısı
	private final YieldCurveCalculator yieldCurveCalculator;
	
	private List<Double> maturities = new ArrayList<Double>();// Vadeye kalan süreleri ( yıl cinsinden) tutan liste
	private List<Double> yields = new ArrayList<Double>(); // Yield değerleri
	private List<LocalDate> maturityDates = new ArrayList<LocalDate>(); // Vade tarihlerini tutacak liste ( tarih cinsinden) 
	
	
	// dependency injection
	public BondCalculator(YieldCurveCalculator yieldCurveCalculator){
		this.yieldCurveCalculator = yieldCurveCalculator;
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

	/**
	 * Bonoların vadesine kalan gün ve yıl sayısını hesaplar ve ekrana yazdırır.
	 *
	 * @param bondList Vadesine kalan zamanı hesaplanacak bono listesi
	 */
	public  void calculateDaysToMaturity(List<BondInstrument> bondList) {
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
	    	validateBond(bond);
	        

	        long daysToMaturity = ChronoUnit.DAYS.between(bond.getBusinessDate(), bond.getEndDate());

	        // Gün sayısını 365'e böler, bu da yıl cinsinden vadesine kalan zamanı verir
	        double yearsToMaturityValue = daysToMaturity / 365.0;
	        yearsToMaturityValue = yieldCurveCalculator.formatToDecimalPlaces(yearsToMaturityValue, 4);
	        return yearsToMaturityValue;
	    
	    } catch (Exception e) {
	        System.err.println("Bir hata oluştu: " + e.getMessage());
	        e.printStackTrace();
	        return 0; // Hata durumunda dönülecek varsayılan değer
	    }
	}
	
	
	public void validateBond(BondInstrument bond) {
		if (bond == null || bond.getStartDate() == null || bond.getEndDate() == null || bond.getBusinessDate() == null) {
			throw new IllegalArgumentException("Bono veya tarih bilgisi eksik.");
		}
		double totalDays = ChronoUnit.DAYS.between(bond.getStartDate(), bond.getEndDate());
		if (totalDays == 0) {
			throw new ArithmeticException("Toplam gün sayısı 0 olamaz.");
		}
	}
	
	// birikmiş faiz tutarının hesaplayan fonksiyon
	public double calculateAccruedInterest(BondInstrument bond) {
		try {
			validateBond(bond);
			double totalDays = ChronoUnit.DAYS.between(bond.getStartDate(), bond.getEndDate());
			long elapsedDays = ChronoUnit.DAYS.between(bond.getStartDate(), bond.getBusinessDate());
			if (bond.getBusinessDate().isAfter(bond.getStartDate())) {
				elapsedDays -= ChronoUnit.DAYS.between(bond.getStartDate(), bond.getBusinessDate());
			}
			double couponPayment = (bond.getCouponRate() / 100) * bond.getNominalValue() / 2; // 6 ayda bir ödeme varsayıyoruz
			return (elapsedDays / totalDays) * couponPayment;
		} catch (Exception e) {
			System.err.println("Bir hata oluştu: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}


	// Kirli fiyatı hesaplayan fonksiyonn
	public double calculateDirtyPrice(BondInstrument bond) {
		try {
			validateBond(bond); // Bono bilgisinin geçerli olup olmadığını kontrol eder
			double accruedInterest = calculateAccruedInterest(bond);
			double dirtyPrice = bond.getSavedPrice() + accruedInterest;
			return dirtyPrice;
		} catch (Exception e) {
			System.err.println("Kirli fiyat hesaplarken bir hata oluştu: " + e.getMessage());
			e.printStackTrace();
			return 0; // Hata durumunda dönülecek varsayılan değer
		}
	}


	// Kirli fiyatları hesaplayan fonksiyon
	public List<Double> calculateDirtyPrices(List<BondInstrument> bonds) {
		if (bonds == null || bonds.isEmpty()) {
			throw new IllegalArgumentException("Bono listesi boş veya null olamaz.");
		}

		// Kirli fiyatlar listesi oluşturuluyor
		List<Double> dirtyPrices = new ArrayList<>();
		// Her bir bono için kirli fiyat hesaplanıyor
		for (BondInstrument bond : bonds) {
			try {
				validateBond(bond); // Bono bilgisinin geçerli olup olmadığını kontrol eder
				double dirtyPrice = calculateDirtyPrice(bond);
				dirtyPrices.add(dirtyPrice); 
			} catch (Exception e) {
				System.err.println("Kirli fiyat hesaplanırken bir hata oluştu: " + e.getMessage());
				e.printStackTrace();
				// Hata durumunda belirli bir işlem yapılması istenirse buraya ekleyebiliriz.
			}
		}
		return dirtyPrices; 
	}



	
	public Map<Double, Double> buildYieldCurveDataMap(List<BondInstrument> bonds) {

		if (bonds == null || bonds.isEmpty()) {
			throw new IllegalArgumentException("Bono listesi boş veya null olamaz.");
		}
		
		Map<Double, Double> yieldCurveDataMap = new TreeMap<>();
		
		for (BondInstrument bond : bonds) {
			validateBond(bond); // Bono bilgisinin geçerli olup olmadığını kontrol eder

			double rawMaturityInYears = yieldCurveCalculator.formatToDecimalPlaces(yearsToMaturity(bond), 4);// Vade yıllarını hesaplar
			double yieldToMaturity = yieldCurveCalculator.calculateYieldToMaturity(rawMaturityInYears, bond);
			LocalDate maturityDate = findMaturityDate(bond.getBusinessDate(), rawMaturityInYears);

			// İsteğe bağlı olarak verileri loglamak
			System.out.println("Maturity: " + rawMaturityInYears + ", YTM: " + yieldToMaturity);

			maturityDates.add(maturityDate);
			maturities.add(rawMaturityInYears);
			yields.add(yieldToMaturity);
			yieldCurveDataMap.put(rawMaturityInYears, yieldToMaturity);
		}

		return yieldCurveDataMap;
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
	
	/*
	static List<Double> maturities = new ArrayList<Double>();// Vade tarihleri
	static List<Double> yields = new ArrayList<Double>(); // Yield değerleri
	static List<LocalDate> maturityDates = new ArrayList<LocalDate>(); // Vade tarihlerini tutacak liste

	public  Map<Double, Double> buildYieldCurveDataMap(List<BondInstrument> bonds) {
		Map<Double, Double> treeMap = new TreeMap<>();

		for (BondInstrument bond : bonds) {

			double maturity = yearsToMaturity(bond);
			maturity = yieldCurveCalculator.formatToDecimalPlaces(maturity, 4);
			// System.out.println(maturity);
			// double maturity = 2.4;
			System.out.println("saved price:" + bond.savedPrice + ", coupon payment:" + bond.couponPayment
					+ ", nominal value:" + bond.nominalValue + ", maturity:" + maturity + ", coupon rate:"
					+ bond.couponRate + "nominal deger" + bond.nominalValue);

			double ytm = yieldCurveCalculator.calculateYTM(bond.savedPrice, bond.couponPayment, bond.nominalValue,
					maturity, bond);
			System.out.println("hoooooooooo" + ytm);
			LocalDate dates = getMaturityDate(bond.businessDate, maturity);

			maturityDates.add(dates);
			System.out.println("yeni end dateler: " + maturityDates);
			maturities.add(maturity);
			yields.add(ytm);
			treeMap.put(maturity, ytm);
		}

		return treeMap;
	}
	*/

}
