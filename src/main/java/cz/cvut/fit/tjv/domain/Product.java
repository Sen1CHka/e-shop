package cz.cvut.fit.tjv.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "products")
public class Product implements EntityWithId<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String description;


    private Double price;


    private Long availableAmount;

    @ManyToMany(mappedBy = "products")
    private Collection<Order> orders;

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

    public Long getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Long availableAmount) {
        this.availableAmount = availableAmount;
    }

}
