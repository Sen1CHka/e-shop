package eshop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Snapshot of the delivery address stored with the order, so later changes
 * in the address book do not rewrite order history.
 */
@Embeddable
public class ShippingAddress {

    @Column(name = "shipping_full_name")
    private String fullName;

    @Column(name = "shipping_street")
    private String street;

    @Column(name = "shipping_city")
    private String city;

    @Column(name = "shipping_zip_code")
    private String zipCode;

    @Column(name = "shipping_country")
    private String country;

    @Column(name = "shipping_phone")
    private String phone;

    public ShippingAddress() {
    }

    public ShippingAddress(String fullName, String street, String city, String zipCode, String country, String phone) {
        this.fullName = fullName;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }
}
