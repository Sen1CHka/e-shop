package eshop.contracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name is too long")
        String fullName,

        @NotBlank(message = "Street is required")
        @Size(max = 120, message = "Street is too long")
        String street,

        @NotBlank(message = "City is required")
        @Size(max = 80, message = "City is too long")
        String city,

        @NotBlank(message = "ZIP code is required")
        @Size(max = 12, message = "ZIP code is too long")
        String zipCode,

        @NotBlank(message = "Country is required")
        @Size(max = 60, message = "Country is too long")
        String country,

        @Size(max = 30, message = "Phone is too long")
        String phone,

        boolean defaultAddress
) {
}
