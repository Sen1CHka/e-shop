package eshop.contracts;

import java.util.Collection;

public class OrderRequest {

    private String username;
    private Collection<Long> products;
    private Integer state;

    public OrderRequest(String username, Collection<Long> products, Integer state) {
        this.username = username;
        this.products = products;
        this.state = state;
    }

    public Collection<Long> getProducts() {
        return products;
    }

    public void setProducts(Collection<Long> products) {
        this.products = products;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
