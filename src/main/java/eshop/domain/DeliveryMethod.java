package eshop.domain;

import java.math.BigDecimal;

public enum DeliveryMethod {
    PICKUP("Store pickup", "Pick up at our Prague store, ready within 24 hours", "0"),
    PARCEL_LOCKER("Parcel locker", "Delivered to a parcel locker in 1-2 working days", "69"),
    COURIER("Courier", "Delivered to your door in 1-2 working days", "119");

    private final String label;
    private final String description;
    private final BigDecimal price;

    DeliveryMethod(String label, String description, String price) {
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
