package eshop.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@Entity
@Table(name = "orders")  // Use a plural name for the table
public class Order implements EntityWithId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", date=" + date +
                ", state=" + state +
                ", products=" + products.stream().map(Product::toString) +
                ", totalPrice=" + totalPrice +
                '}';
    }

    @ManyToMany
    @JoinTable(
            name = "product_in_orders",
            joinColumns = @JoinColumn(name = "orders_with_product"),
            inverseJoinColumns = @JoinColumn(name = "product_in_order")
    )
    private Collection<Product> products;

    private Double totalPrice;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = BigDecimal.valueOf(totalPrice)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void calculateTotalPrice() {
        if (products != null && !products.isEmpty()) {
            totalPrice = products.stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
        } else {
            totalPrice = 0.0;
        }
    }
    public void deleteProductById(Long id)
    {
        if(products!=null)
        {
            for (Iterator<Product> iterator = products.iterator(); iterator.hasNext();) {
                Product product = iterator.next();
                if (product.getId().equals(id)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }
}