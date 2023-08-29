package com.project.yieldcurve;

//noktadan sonra 4 basamak almak için
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.springframework.stereotype.Service;

//
@Service
public class YieldCurveCalculator {
	


	public double calculateYieldToMaturity(double maturity ,BondInstrument bond) {
		double ytm;
		if (bond.isZeroCoupon()) { // Zero kuponlu bono mu kontrolü
			ytm = calculateYTMForZeroCouponBond(maturity ,bond);
		} else {
			ytm = calculateYTMForCouponBond(maturity, bond);
		}
		return ytm;
	}
	
	public double calculateYTMForZeroCouponBond(double yearsToMaturity,BondInstrument bond ) {
		System.out.println("kontrol");
		double nominalValue =bond.getNominalValue();
		double Price = bond.getSavedPrice();
		
		double ytm = Math.pow((nominalValue / Price), (1.0 / yearsToMaturity)) - 1;
		ytm = formatToDecimalPlaces(ytm, 4);
		return ytm * 100; // Yüzde olarak dönüş
	}
	


	public  double calculateYTMForCouponBond(double maturity , BondInstrument bond) {

		
		double Price = bond.getSavedPrice();
		double CouponPayment = bond.getCouponPayment(); 
		double FaceValue = bond.getNominalValue();
		
		

		double numberOfCouponPayments = calculateDatesOfCouponPayments(bond).size();
		
		int frequency;


		if (maturity < 0.5) {
			frequency = 1; // 1 yıldan az vadeli tahviller için yıllık kupon ödeme sayısı 1
		} else {
			frequency = 2; // 1 yıl ve üzeri vadeli tahviller için yıllık kupon ödeme sayısı 2
		}

		UnivariateFunction function = new UnivariateFunction() {
			@Override
			public double value(double r) {
				double sum = 0.0;
				for (double j = 1; j <= numberOfCouponPayments ; j++) {
					sum += (CouponPayment / frequency) / Math.pow(1 + r / frequency, j);			
				}
				sum += FaceValue / Math.pow(1 + r / frequency, numberOfCouponPayments );
				//System.out.println(couponDates.size());
				return Price - sum;
			}
		};

		UnivariateSolver solver = new BrentSolver();
		double lowerBound = 0.0; // Alt sınır
		double upperBound = 0.8; // Üst sınır
		double initialGuess = 0.01; // İlk tahmin

		double ytm = solver.solve(1000, function, lowerBound, upperBound, initialGuess);
		ytm = ytm * 100; // yüzde olarak göstermek için çarptık

		ytm = formatToDecimalPlaces(ytm, 4);
		return ytm;
	}
	
	public List<LocalDate> calculateDatesOfCouponPayments(BondInstrument bond) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate endDate = bond.getEndDate();
		LocalDate businessDate = bond.getBusinessDate();

		// 6 aylık periyotlarla geriye giderek kupon ödemelerini saklama
		List<LocalDate> couponDates = new ArrayList<LocalDate>();
		LocalDate tempDate = endDate;
		while (tempDate.isAfter(businessDate)) {
			couponDates.add(tempDate);
			tempDate = tempDate.minusMonths(6);
		}
		
		return couponDates;
		
	}

	public double formatToDecimalPlaces(double number, int decimalPlaces) {
		StringBuilder pattern = new StringBuilder("#.");
		for (int i = 0; i < decimalPlaces; i++) {
			pattern.append('#');
		}
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		symbols.setDecimalSeparator('.'); // Ondalık ayırıcı olarak nokta kullan
		DecimalFormat df = new DecimalFormat(pattern.toString(), symbols); // "decimalPlaces" istenen ondalık basamak
																			// sayisi
		return Double.parseDouble(df.format(number));
	}

	public double linearSplineInterpolation(LocalDate date1, double yield1, LocalDate date2, double yield2,
			LocalDate targetDate) {
		// İki tarih arasındaki farkı hesapla
		long daysBetweenDates = ChronoUnit.DAYS.between(date1, date2);
		long daysToTarget = ChronoUnit.DAYS.between(date1, targetDate);

		// Doğrusal interpolasyon kullanarak hedef tarihe karşılık gelen yield değerini
		// hesapla
		double interpolatedYield = yield1 + (yield2 - yield1) * daysToTarget / daysBetweenDates;

		return interpolatedYield;
	}

	
	/*
	public static void main(String[] args) {
		double Price = 101208; // Tahvilin şimdiki piyasa fiyatı.
		double CouponPayment = 8600; // kupon ödemesi
		double FaceValue = 100000; // nominal değer
		double maturity = 1; // 2 yıl 440 gün vade

		double ytm = calculateYTM(Price, CouponPayment, FaceValue, maturity);
		System.out.println("Yield to Maturity: " + ytm + "%");
	}
*/
	
}