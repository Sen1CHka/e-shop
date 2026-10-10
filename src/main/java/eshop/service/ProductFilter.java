package eshop.service;

import java.math.BigDecimal;

public record ProductFilter(
        String query,
        Long categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        boolean inStockOnly,
        boolean includeInactive,
        String sort
) {
}
