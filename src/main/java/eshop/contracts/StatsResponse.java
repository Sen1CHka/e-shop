package eshop.contracts;

import eshop.domain.OrderState;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record StatsResponse(
        long productCount,
        long userCount,
        long orderCount,
        BigDecimal revenue,
        Map<OrderState, Long> ordersByState,
        List<ProductResponse> lowStock,
        List<TopProduct> topProducts,
        List<OrderResponse> recentOrders
) {
    public record TopProduct(String name, long quantity) {
    }
}
