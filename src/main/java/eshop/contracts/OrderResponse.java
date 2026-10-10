package eshop.contracts;

import eshop.domain.DeliveryMethod;
import eshop.domain.Order;
import eshop.domain.OrderState;
import eshop.domain.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String username,
        String realName,
        LocalDateTime date,
        OrderState state,
        List<OrderItemResponse> items,
        int itemCount,
        BigDecimal itemsPrice,
        DeliveryMethod deliveryMethod,
        String deliveryLabel,
        BigDecimal deliveryPrice,
        PaymentMethod paymentMethod,
        String paymentLabel,
        BigDecimal paymentPrice,
        BigDecimal totalPrice,
        ShippingDto shipping,
        String note
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getUsername(),
                order.getUser().getRealName(),
                order.getDate(),
                order.getState(),
                order.getItems().stream().map(OrderItemResponse::from).toList(),
                order.getItems().stream().mapToInt(i -> i.getQuantity()).sum(),
                order.getItemsPrice(),
                order.getDeliveryMethod(),
                order.getDeliveryMethod().getLabel(),
                order.getDeliveryPrice(),
                order.getPaymentMethod(),
                order.getPaymentMethod().getLabel(),
                order.getPaymentPrice(),
                order.getTotalPrice(),
                ShippingDto.from(order.getShipping()),
                order.getNote()
        );
    }
}
