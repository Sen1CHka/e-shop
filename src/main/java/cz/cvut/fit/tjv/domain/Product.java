package cz.cvut.fit.tjv.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "products")
public class Product implements EntityWithId<Long>{

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long avaliableAmount;
    @ManyToMany
    @JoinTable(
            name = "product_in_order",
            joinColumns = @JoinColumn(name = "orders_with_product"),
            inverseJoinColumns = @JoinColumn(name = "product_in_order")
    )
    private final Collection<Order> orders = new ArrayList<>();

    @Override
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

    public void setDescription(String discription) {
        this.description = discription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getAvaliableAmount() {
        return avaliableAmount;
    }

    public void setAvaliableAmount(Long avaliableAmount) {
        this.avaliableAmount = avaliableAmount;
    }

}
