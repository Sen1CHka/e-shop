package eshop.contracts;

import eshop.domain.ShippingAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ShippingDto(
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

        @NotBlank(message = "Phone is required")
        @Size(max = 30, message = "Phone is too long")
        String phone
) {
    public static ShippingDto from(ShippingAddress shipping) {
        if (shipping == null) return null;
        return new ShippingDto(shipping.getFullName(), shipping.getStreet(), shipping.getCity(),
                shipping.getZipCode(), shipping.getCountry(), shipping.getPhone());
    }

    public ShippingAddress toEntity() {
        return new ShippingAddress(fullName, street, city, zipCode, country, phone);
    }
}
