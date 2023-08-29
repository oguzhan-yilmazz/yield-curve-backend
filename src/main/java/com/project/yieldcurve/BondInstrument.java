package com.project.yieldcurve;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class BondInstrument {
	    private LocalDate businessDate;  // işlem tarihi
	    private String seriesId;   // bono ve tahvilin seri id'si.
	    private double savedPrice;   // bono ve tahvilin bugün kü değeri.(clear price 
	    private LocalDate startDate;   // bono veya tahvilin başlangıç tarihi
	    private LocalDate endDate;    // bono veya tahvilin başlangıç tarihi
	    //double interestRate; // Yıllık faiz oranı  ( coupon rate ile aynı şey diyebilirz ? ) 
	    private double couponRate;
	    private double couponPayment;
		private double nominalValue = 100000; // Nominal değerr

  
	    @Override
	    public String toString() {
	        return "BondInstrument{" +
	                "business_date=" + businessDate +
	                ", series_id='" + seriesId + '\'' +
	                ", saved_price=" + savedPrice +
	                ", start_date=" + startDate +
	                ", end_date=" + endDate +
	                ", couponrate=" + couponRate +
	                ", couponPayment=" + couponPayment+
	                '}';
	    }
	    
	    public BondInstrument() {}
	    
	    public BondInstrument(LocalDate business_date, String series_id, double saved_price, LocalDate start_date, LocalDate end_date, double couponrate) {
	        this.businessDate = business_date;
	        this.seriesId = series_id;
	        this.savedPrice = saved_price;
	        this.startDate = start_date;
	        this.endDate = end_date;
	        this.couponRate = couponrate/100;
	        this.couponPayment = couponRate * nominalValue *2;  // bize verilen couponrate ler 6 aylık olduğu için onu 2 ile çarparak yıl olarak hesaba katarız.
	    }
	    
 
	    public LocalDate getBusinessDate() {
			return businessDate;
		}

		public void setBusinessDate(LocalDate businessDate) {
			this.businessDate = businessDate;
		}

		public String getSeriesId() {
			return seriesId;
		}

		public void setSeriesId(String seriesId) {
			this.seriesId = seriesId;
		}

		public double getSavedPrice() {
			return savedPrice;
		}

		public void setSavedPrice(double savedPrice) {
			this.savedPrice = savedPrice;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}

		public double getCouponRate() {
			return couponRate;
		}

		public void setCouponRate(double couponRate) {
			this.couponRate = couponRate;
		}

		public double getCouponPayment() {
			return couponPayment;
		}

		public void setCouponPayment(double couponPayment) {
			this.couponPayment = couponPayment;
		}

		public double getNominalValue() {
			return nominalValue;
		}

		public void setNominalValue(double nominalValue) {
			this.nominalValue = nominalValue;
		}

		public boolean isZeroCoupon() {
	        if (this.couponRate == 0.0) {
	            return true;
	        }

	        return false;
	    }
	    

	    

}