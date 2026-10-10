package eshop.contracts;

import eshop.domain.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        String imageUrl,
        String categoryIcon,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal
) {
    public static OrderItemResponse from(OrderItem item) {
        var product = item.getProduct();
        return new OrderItemResponse(
                product.getId(),
                item.getProductName(),
                product.getImageUrl(),
                product.getCategory() == null ? null : product.getCategory().getIcon(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }
}
