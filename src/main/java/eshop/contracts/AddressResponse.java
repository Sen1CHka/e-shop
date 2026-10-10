package eshop.contracts;

import eshop.domain.Address;

public record AddressResponse(
        Long id,
        String fullName,
        String street,
        String city,
        String zipCode,
        String country,
        String phone,
        boolean defaultAddress
) {
    public static AddressResponse from(Address address) {
        return new AddressResponse(address.getId(), address.getFullName(), address.getStreet(), address.getCity(),
                address.getZipCode(), address.getCountry(), address.getPhone(), address.isDefaultAddress());
    }
}
