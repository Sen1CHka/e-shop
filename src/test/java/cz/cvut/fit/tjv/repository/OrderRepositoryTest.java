package cz.cvut.fit.tjv.repository;

import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.domain.OrderState;
import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class OrderRepositoryTest {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserUsername() {

        var user1 = new User();
        user1.setUsername("user123");
        user1.setEmail("user123@gmail.com");
        user1.setRealName("User 123");
        user1.setPassword("user123");
        var user2 = new User();
        user2.setUsername("user456");
        user2.setEmail("user456@gmail.com");
        user2.setRealName("User 456");
        user2.setPassword("user456");


        var order1 = new Order();
        var order2 = new Order();
        var order3 = new Order();
        var order4 = new Order();

        order1.setUser(user1);
        order2.setUser(user1);
        order3.setUser(user2);
        order4.setUser(user2);

        order1.setId(1L);
        order2.setId(5L);
        order3.setId(10L);
        order4.setId(15L);

        order1.setDate(LocalDateTime.now());
        order2.setDate(LocalDateTime.now());
        order3.setDate(LocalDateTime.now());
        order4.setDate(LocalDateTime.now());

        order1.setState(OrderState.PROCESSED);
        order2.setState(OrderState.PROCESSED);
        order3.setState(OrderState.PROCESSED);
        order4.setState(OrderState.PROCESSED);

        order1.setTotalPrice(0D);
        order2.setTotalPrice(0D);
        order3.setTotalPrice(0D);
        order4.setTotalPrice(0D);


        userRepository.save(user1);
        userRepository.save(user2);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);



        var foundOrders = orderRepository.findByUserUsername(user1.getUsername());

        Assertions.assertEquals(2, foundOrders.size());
        Assertions.assertIterableEquals(List.of(order1, order2), foundOrders);
    }

    @Test
    public void testFindByDateBefore() {

        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        var user1 = new User();
        user1.setUsername("user123");
        user1.setEmail("user123@gmail.com");
        user1.setRealName("User 123");
        user1.setPassword("user123");
        var user2 = new User();
        user2.setUsername("user456");
        user2.setEmail("user456@gmail.com");
        user2.setRealName("User 456");
        user2.setPassword("user456");

        var order1 = new Order();
        var order2 = new Order();
        var order3 = new Order();
        var order4 = new Order();

        order1.setUser(user1);
        order2.setUser(user1);
        order3.setUser(user2);

        order1.setId(1L);
        order2.setId(2L);
        order3.setId(3L);

        order1.setDate(date.minusDays(1));
        order2.setDate(date);
        order3.setDate(date.plusDays(1));

        order1.setState(OrderState.PROCESSED);
        order2.setState(OrderState.PROCESSED);
        order3.setState(OrderState.PROCESSED);

        order1.setTotalPrice(0D);
        order2.setTotalPrice(0D);
        order3.setTotalPrice(0D);

        userRepository.save(user1);
        userRepository.save(user2);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        var foundOrders = orderRepository.findByDateBefore(date);

        Assertions.assertIterableEquals(List.of(order1, order2), foundOrders);
    }

}
