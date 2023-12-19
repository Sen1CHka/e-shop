package cz.cvut.fit.tjv.domain;
import jakarta.persistence.*;

import java.util.Collection;


@Entity
@Table(name = "products")
public class Product implements EntityWithId<Long>{

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String discription;
    private Double price;
    private Long avaliableAmount;
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

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
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
