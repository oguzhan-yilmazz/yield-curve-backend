package com.project.yieldcurve.service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.yieldcurve.BondInstrument;
import com.project.yieldcurve.CashFlow;


@Service
public class CashFlowService {
    public List<CashFlow> generateCashFlowsForBond(BondInstrument bond) {
        List<CashFlow> cashFlows = new ArrayList<>();       
       // Kupon ödeme tarihlerini hesapla
        List<LocalDate> couponDates = calculateDatesOfCouponPayments(bond);
        // Her tarih için bir CashFlow objesi oluştur
        for(LocalDate date : couponDates) {
            CashFlow cashFlow = new CashFlow();
            cashFlow.setDate(date);
            cashFlow.setAmount(bond.getCouponPayment());
            cashFlows.add(cashFlow);
        }
        return cashFlows;
    }
    public List<LocalDate> calculateDatesOfCouponPayments(BondInstrument bond) {
        LocalDate endDate = bond.getEndDate();
        LocalDate businessDate = bond.getBusinessDate();
        List<LocalDate> couponDates = new ArrayList<>();
        LocalDate tempDate = endDate;     
        while (tempDate.isAfter(businessDate)) {
            couponDates.add(tempDate);
            tempDate = tempDate.minusMonths(6);
        }     
        return couponDates;
    }   
}
