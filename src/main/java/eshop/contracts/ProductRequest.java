package eshop.contracts;

public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer availableAmount;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description, Double price, Integer availableAmount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableAmount = availableAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }
}
