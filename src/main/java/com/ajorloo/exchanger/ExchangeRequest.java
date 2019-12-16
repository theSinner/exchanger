package com.ajorloo.exchanger;

import java.util.List;
import java.util.Arrays;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

interface Validable {
    void isValid();
}

class ExchangeRequest implements Validable {
    private String sourceUnit;
    private String targetUnit;
    private Double amount;
    final static List<String> availableUnits = Arrays.asList("EUR", "CAD", "HKD", "ISK", "PHP", "DKK", "HUF", "CZK",
            "AUD", "RON", "SEK", "IDR", "INR", "BRL", "RUB", "HRK", "JPY", "THB", "CHF", "SGD", "PLN", "BGN", "TRY",
            "CNY", "NOK", "NZD", "ZAR", "USD", "MXN", "ILS", "GBP", "KRW", "MYR");

    ExchangeRequest(String sUnit, String tUnit, Double amnt) {
        this.sourceUnit = sUnit;
        this.targetUnit = tUnit;
        if (this.sourceUnit != null) {
            this.sourceUnit = this.sourceUnit.toUpperCase();
        }
        if (this.targetUnit != null) {
            this.targetUnit = this.targetUnit.toUpperCase();
        }
        this.amount = amnt;
    }

    public static String format(Double value) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(value);
    }

    /**
     * @return the sourceUnit
     */
    public String getSourceUnit() {
        return sourceUnit;
    }

    /**
     * @return the targetUnit
     */
    public String getTargetUnit() {
        return targetUnit;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    public void isValid() {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is not valid");
        }
        if (sourceUnit.isEmpty()) {
            throw new IllegalArgumentException("Source unit must not be empty");
        } else if (!availableUnits.contains(sourceUnit)) {
            throw new IllegalArgumentException("Entered source unit is not acceptable");
        }
        if (targetUnit.isEmpty()) {
            throw new IllegalArgumentException("Source unit must not be empty");
        } else if (!availableUnits.contains(targetUnit)) {
            throw new IllegalArgumentException("Entered source unit is not acceptable");
        }
    }
}