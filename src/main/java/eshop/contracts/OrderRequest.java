package eshop.contracts;

import eshop.domain.DeliveryMethod;
import eshop.domain.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotEmpty(message = "Order must contain at least one product")
        @Valid
        List<OrderItemRequest> items,

        @NotNull(message = "Delivery method is required")
        DeliveryMethod deliveryMethod,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Delivery address is required")
        @Valid
        ShippingDto shipping,

        @Size(max = 1000, message = "Note is too long")
        String note
) {
}
