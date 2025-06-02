package eshop.service;

import eshop.contracts.OrderResponse;
import eshop.domain.OrderState;
import eshop.domain.Product;
import eshop.domain.User;
import eshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderResponseServiceImplUnitTest {
    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private OrderRepository orderRepository;

    User user;
    eshop.domain.Order order1;
    eshop.domain.Order order2;

    @BeforeEach
    public void setUp() {
        String userId = "user123";
        user = new User();
        user.setUsername(userId);

        order1 = new eshop.domain.Order();
        order1.setId(0L);
        order1.setUser(user);
        order1.setProducts(List.of(new Product(),new Product(), new Product()));
        order1.setDate(LocalDateTime.now());
        order1.setState(OrderState.DELIVERED);
        order2= new eshop.domain.Order();
        order2.setId(0L);
        order2.setUser(user);
        order2.setProducts(List.of(new Product()));
        order2.setDate(LocalDateTime.now());
        order2.setState(OrderState.SENT);
    }

    @Test
    public void readAllByAuthorTest() {

        List<eshop.domain.Order> orders = List.of(order1, order2);

        when(orderRepository.findByUserUsername(user.getUsername())).thenReturn(orders);

        Collection<eshop.domain.Order> result = orderService.getAllByAuthor(user.getUsername());

        verify(orderRepository).findByUserUsername(user.getUsername());
        assertEquals(orders, result);
    }

    @Test
    public void readAllByDateTest() {
        LocalDateTime date = LocalDateTime.now();

        List<eshop.domain.Order> orders = new ArrayList<>(List.of(order1, order2));

        when(orderRepository.findByDateBefore(date)).thenReturn(orders);

        Collection<eshop.domain.Order> result = orderService.getByDateBefore(date);

        verify(orderRepository).findByDateBefore(date);
        assertEquals(orders, result);
    }

    @Test
    public void convertOrderToDtoTest() {
        eshop.domain.Order order = new eshop.domain.Order();
        OrderResponse dto = OrderServiceImpl.convertOrderToDto(order);
        assertNotNull(dto);
    }
}
