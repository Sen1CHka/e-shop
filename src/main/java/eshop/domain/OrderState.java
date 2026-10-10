package eshop.domain;

public enum OrderState {
    NEW,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELLED;
    }
}
