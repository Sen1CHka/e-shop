package eshop.contracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 60, message = "Name is too long")
        String name,

        @Size(max = 500, message = "Description is too long")
        String description,

        @Size(max = 16, message = "Icon is too long")
        String icon
) {
}
