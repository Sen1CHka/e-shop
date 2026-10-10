package eshop.repository;

import eshop.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void findLessPriceTest() {
        Product cheap = product("product1", "100");
        product("product2", "200");

        var products = productRepository.findLessPrice(new BigDecimal("150"));

        assertThat(products).containsOnly(cheap);
    }

    @Test
    public void findByDateBeforeTest() {
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        User user = user("user123");
        Order yesterday = order(user, date.minusDays(1));
        Order today = order(user, date);
        order(user, date.plusDays(1));

        var foundOrders = orderRepository.findByDateBefore(date);

        assertThat(foundOrders).containsExactlyInAnyOrder(yesterday, today);
    }

    @Test
    public void findByUserUsernameReturnsNewestFirst() {
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        User user1 = user("user123");
        User user2 = user("user456");
        Order older = order(user1, date.minusDays(2));
        Order newer = order(user1, date);
        order(user2, date);

        var foundOrders = orderRepository.findByUserUsernameOrderByDateDesc("user123");

        assertThat(foundOrders).containsExactly(newer, older);
    }

    @Test
    public void ratingStatsAreGroupedByProduct() {
        Product rated = product("rated", "100");
        Product unrated = product("unrated", "100");
        reviewRepository.save(new Review(rated, user("user123"), 5, "Great"));
        reviewRepository.save(new Review(rated, user("user456"), 2, null));

        var stats = reviewRepository.ratingStats(List.of(rated.getId(), unrated.getId()));

        assertThat(stats).hasSize(1);
        assertThat(stats.get(0).getProductId()).isEqualTo(rated.getId());
        assertThat(stats.get(0).getAverage()).isEqualTo(3.5);
        assertThat(stats.get(0).getCount()).isEqualTo(2);
    }

    private Product product(String name, String price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        product.setAvailableAmount(10);
        return productRepository.save(product);
    }

    private User user(String username) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@gmail.com");
        user.setRealName("User " + username);
        user.setPassword("secret");
        return userRepository.save(user);
    }

    private Order order(User user, LocalDateTime date) {
        Order order = new Order();
        order.setUser(user);
        order.setDate(date);
        order.setDeliveryMethod(DeliveryMethod.PICKUP);
        order.setPaymentMethod(PaymentMethod.CARD);
        return orderRepository.save(order);
    }
}
