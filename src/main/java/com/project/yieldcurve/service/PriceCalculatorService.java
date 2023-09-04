package com.project.yieldcurve.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondInstrument;


@Service
public class PriceCalculatorService {
	
	
	private final ValidationService validationService; // final ile immutuble olmasını sağlıyoruz.
	private final InterestCalculatorService interestCalculatorService;
	
	
	// constructor injection
	public PriceCalculatorService(ValidationService validationService,InterestCalculatorService interestCalculatorService ) {
		this.validationService= validationService;
		this.interestCalculatorService = interestCalculatorService;
	}

	// Kirli fiyatı hesaplayan fonksiyonn
	public double calculateDirtyPrice(BondInstrument bond) {
		try {
			validationService.validateBond(bond); // Bono bilgisinin geçerli olup olmadığını kontrol eder
			double accruedInterest = interestCalculatorService.calculateAccruedInterest(bond);
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
				validationService.validateBond(bond); // Bono bilgisinin geçerli olup olmadığını kontrol eder
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
}
