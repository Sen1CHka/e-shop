package cz.cvut.fit.tjv.contracts;

public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer avaliableAmount;

    public ProductDTO() {
    }
    public ProductDTO(Integer id, String name, String description, Double price, Integer avaliableAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.avaliableAmount = avaliableAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getAvaliableAmount() {
        return avaliableAmount;
    }

    public void setAvaliableAmount(Integer avaliableAmount) {
        this.avaliableAmount = avaliableAmount;
    }
}
