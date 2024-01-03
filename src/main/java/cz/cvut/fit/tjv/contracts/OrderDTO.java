package cz.cvut.fit.tjv.contracts;

import cz.cvut.fit.tjv.domain.Product;

import java.time.LocalDateTime;
import java.util.Collection;

public class OrderDTO {

    private Integer id;


    private String username;

    private Collection<ProductDTO> products;

    private LocalDateTime date;

    private String state;

    public OrderDTO() {
    }

    public OrderDTO(Integer id, String username, Collection<ProductDTO> products, LocalDateTime date, String state) {
        this.id = id;
        this.username = username;
        this.products = products;
        this.date = date;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public Collection<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Collection<ProductDTO> products) {
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
