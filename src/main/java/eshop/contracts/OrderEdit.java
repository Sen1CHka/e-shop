package eshop.contracts;

import java.util.Collection;

public class OrderEdit {
    private Collection<Long> products;
    private Integer state;

    public OrderEdit(Collection<Long> products, Integer state) {
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
}
