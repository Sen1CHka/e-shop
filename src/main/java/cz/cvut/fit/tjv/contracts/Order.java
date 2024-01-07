package cz.cvut.fit.tjv.contracts;

import java.time.LocalDateTime;
import java.util.Collection;

public class Order {

    private Integer id;

    private String username;

    private Collection<Product> products;

    private LocalDateTime date;

    private String state;

    private Integer stateId;

    private Double totalPrice;

    public Order() {
    }

    public Order(Integer id, String username, Collection<Product> products, LocalDateTime date, String state, Integer stateId, Double totalPrice) {
        this.id = id;
        this.username = username;
        this.products = products;
        this.date = date;
        this.state = state;
        this.stateId = stateId;
        this.totalPrice = totalPrice;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
