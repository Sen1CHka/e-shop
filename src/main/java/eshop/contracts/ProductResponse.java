package eshop.contracts;

import eshop.domain.Category;
import eshop.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer availableAmount,
        String brand,
        String imageUrl,
        Long categoryId,
        String categoryName,
        String categoryIcon,
        boolean active,
        LocalDateTime createdAt,
        double averageRating,
        long reviewCount
) {
    public static ProductResponse from(Product product, double averageRating, long reviewCount) {
        Category category = product.getCategory();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableAmount(),
                product.getBrand(),
                product.getImageUrl(),
                category == null ? null : category.getId(),
                category == null ? null : category.getName(),
                category == null ? null : category.getIcon(),
                product.isActive(),
                product.getCreatedAt(),
                Math.round(averageRating * 10) / 10.0,
                reviewCount
        );
    }
}
