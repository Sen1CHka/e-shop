package cz.cvut.fit.tjv.contracts;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer availableAmount;

    public Product() {
    }
    public Product(Long id, String name, String description, Double price, Integer availableAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableAmount = availableAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setAvailableAmount(Integer avaliableAmount) {
        this.availableAmount = avaliableAmount;
    }
}
