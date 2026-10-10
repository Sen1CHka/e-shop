package eshop.service;

import eshop.contracts.OrderItemRequest;
import eshop.contracts.OrderRequest;
import eshop.contracts.OrderResponse;
import eshop.contracts.ShippingDto;
import eshop.domain.*;
import eshop.exception.ApiException;
import eshop.repository.ProductRepository;
import eshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private Product laptop;
    private Product cable;

    @BeforeEach
    public void setUp() {
        user("customer");
        user("stranger");
        laptop = product("Laptop", "20000", 3);
        cable = product("Cable", "249.50", 10);
    }

    @Test
    public void createReservesStockAndComputesPriceOnServer() {
        OrderResponse order = orderService.create("customer", request(
                DeliveryMethod.COURIER, PaymentMethod.CASH_ON_DELIVERY,
                new OrderItemRequest(laptop.getId(), 1), new OrderItemRequest(cable.getId(), 2)));

        assertThat(order.state()).isEqualTo(OrderState.NEW);
        assertThat(order.itemsPrice()).isEqualByComparingTo("20499");
        // 20 499 + courier 119 + cash on delivery 39
        assertThat(order.totalPrice()).isEqualByComparingTo("20657");
        assertThat(order.itemCount()).isEqualTo(3);
        assertThat(laptop.getAvailableAmount()).isEqualTo(2);
        assertThat(cable.getAvailableAmount()).isEqualTo(8);
    }

    @Test
    public void createSumsQuantitiesOfRepeatedProduct() {
        OrderResponse order = orderService.create("customer", request(
                DeliveryMethod.PICKUP, PaymentMethod.CARD,
                new OrderItemRequest(cable.getId(), 2), new OrderItemRequest(cable.getId(), 3)));

        assertThat(order.items()).hasSize(1);
        assertThat(order.items().get(0).quantity()).isEqualTo(5);
        assertThat(cable.getAvailableAmount()).isEqualTo(5);
    }

    @Test
    public void createFailsWhenStockIsNotSufficient() {
        OrderRequest request = request(DeliveryMethod.PICKUP, PaymentMethod.CARD,
                new OrderItemRequest(laptop.getId(), 4));

        assertThatThrownBy(() -> orderService.create("customer", request))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Only 3 pcs")
                .extracting(e -> ((ApiException) e).getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(laptop.getAvailableAmount()).isEqualTo(3);
    }

    @Test
    public void createFailsForHiddenProduct() {
        laptop.setActive(false);
        OrderRequest request = request(DeliveryMethod.PICKUP, PaymentMethod.CARD,
                new OrderItemRequest(laptop.getId(), 1));

        assertThatThrownBy(() -> orderService.create("customer", request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    public void cancelReturnsGoodsToStock() {
        OrderResponse order = orderService.create("customer", request(
                DeliveryMethod.PICKUP, PaymentMethod.CARD, new OrderItemRequest(laptop.getId(), 2)));

        OrderResponse cancelled = orderService.cancel(order.id(), "customer");

        assertThat(cancelled.state()).isEqualTo(OrderState.CANCELLED);
        assertThat(laptop.getAvailableAmount()).isEqualTo(3);
    }

    @Test
    public void shippedOrderCannotBeCancelledByCustomer() {
        OrderResponse order = orderService.create("customer", request(
                DeliveryMethod.PICKUP, PaymentMethod.CARD, new OrderItemRequest(laptop.getId(), 1)));
        orderService.updateState(order.id(), OrderState.SHIPPED);

        assertThatThrownBy(() -> orderService.cancel(order.id(), "customer"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("no longer be cancelled");
    }

    @Test
    public void finalStateCannotBeChanged() {
        OrderResponse order = orderService.create("customer", request(
                DeliveryMethod.PICKUP, PaymentMethod.CARD, new OrderItemRequest(laptop.getId(), 1)));
        orderService.updateState(order.id(), OrderState.DELIVERED);

        assertThatThrownBy(() -> orderService.updateState(order.id(), OrderState.NEW))
                .isInstanceOf(ApiException.class);
    }

    @Test
    public void customerSeesOnlyOwnOrders() {
        OrderResponse order = orderService.create("customer", request(
                DeliveryMethod.PICKUP, PaymentMethod.CARD, new OrderItemRequest(laptop.getId(), 1)));

        assertThat(orderService.getByUser("stranger")).isEmpty();
        assertThatThrownBy(() -> orderService.get(order.id(), "stranger", false))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThatThrownBy(() -> orderService.cancel(order.id(), "stranger"))
                .isInstanceOf(ApiException.class);
        assertThat(orderService.get(order.id(), "stranger", true).id()).isEqualTo(order.id());
    }

    @Test
    public void searchFiltersByStateAndCustomer() {
        OrderResponse first = orderService.create("customer", request(
                DeliveryMethod.PICKUP, PaymentMethod.CARD, new OrderItemRequest(laptop.getId(), 1)));
        orderService.create("stranger", request(
                DeliveryMethod.PICKUP, PaymentMethod.CARD, new OrderItemRequest(cable.getId(), 1)));
        orderService.updateState(first.id(), OrderState.PROCESSING);

        assertThat(orderService.search(null, null, null)).hasSize(2);
        assertThat(orderService.search(OrderState.PROCESSING, null, null))
                .extracting(OrderResponse::id).containsExactly(first.id());
        assertThat(orderService.search(null, "STRANG", null))
                .extracting(OrderResponse::username).containsExactly("stranger");
        assertThat(orderService.search(null, null, java.time.LocalDate.now().minusDays(1))).isEmpty();
    }

    private OrderRequest request(DeliveryMethod delivery, PaymentMethod payment, OrderItemRequest... items) {
        return new OrderRequest(List.of(items), delivery, payment,
                new ShippingDto("Test User", "Technicka 2", "Praha", "160 00", "Czech Republic", "+420 600 000 000"),
                null);
    }

    private void user(String username) {
        User user = new User();
        user.setUsername(username);
        user.setRealName("Test " + username);
        user.setEmail(username + "@example.com");
        user.setPassword("secret");
        userRepository.save(user);
    }

    private Product product(String name, String price, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        product.setAvailableAmount(stock);
        return productRepository.save(product);
    }
}
