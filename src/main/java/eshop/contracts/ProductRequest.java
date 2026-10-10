package eshop.contracts;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name is too long")
        String name,

        @Size(max = 2000, message = "Description is too long")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be positive")
        @Digits(integer = 8, fraction = 2, message = "Price may have at most 2 decimal places")
        BigDecimal price,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer availableAmount,

        @Size(max = 80, message = "Brand is too long")
        String brand,

        @Size(max = 1000, message = "Image URL is too long")
        String imageUrl,

        Long categoryId,

        Boolean active
) {
}
