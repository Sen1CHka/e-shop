package eshop.contracts;

import eshop.domain.DeliveryMethod;
import eshop.domain.PaymentMethod;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public record CheckoutOptionsResponse(List<Option> deliveryMethods, List<Option> paymentMethods) {

    public record Option(String code, String label, String description, BigDecimal price) {
    }

    public static CheckoutOptionsResponse all() {
        return new CheckoutOptionsResponse(
                Arrays.stream(DeliveryMethod.values())
                        .map(m -> new Option(m.name(), m.getLabel(), m.getDescription(), m.getPrice()))
                        .toList(),
                Arrays.stream(PaymentMethod.values())
                        .map(m -> new Option(m.name(), m.getLabel(), m.getDescription(), m.getPrice()))
                        .toList()
        );
    }
}
