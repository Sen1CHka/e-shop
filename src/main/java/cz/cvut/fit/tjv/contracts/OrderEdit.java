package cz.cvut.fit.tjv.contracts;

import java.time.LocalDateTime;
import java.util.Collection;

public class OrderEdit {
    private Collection<Long> products;

    private LocalDateTime date;

    private String state;

    public OrderEdit() {
    }

    public OrderEdit(Collection<Long> products, LocalDateTime date, String state) {
        this.products = products;
        this.date = date;
        this.state = state;
    }

    public Collection<Long> getProducts() {
        return products;
    }

    public void setProducts(Collection<Long> products) {
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
