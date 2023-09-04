package com.project.yieldcurve;

import java.time.LocalDate;

public class CashFlow {
    private LocalDate date;
    private double amount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

