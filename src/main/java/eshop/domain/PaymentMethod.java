package eshop.domain;

import java.math.BigDecimal;

public enum PaymentMethod {
    CARD("Card online", "Pay by card right after placing the order", "0"),
    BANK_TRANSFER("Bank transfer", "We ship once the payment arrives", "0"),
    CASH_ON_DELIVERY("Cash on delivery", "Pay in cash or by card on delivery", "39");

    private final String label;
    private final String description;
    private final BigDecimal price;

    PaymentMethod(String label, String description, String price) {
        this.label = label;
        this.description = description;
        this.price = new BigDecimal(price);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
