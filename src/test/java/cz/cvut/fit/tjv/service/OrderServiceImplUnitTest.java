package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.Order;
import cz.cvut.fit.tjv.domain.OrderState;
import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.repository.OrderRepository;
import cz.cvut.fit.tjv.repository.ProductRepository;
import cz.cvut.fit.tjv.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceImplUnitTest {
    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private OrderRepository orderRepository;

    User user;
    cz.cvut.fit.tjv.domain.Order order1;
    cz.cvut.fit.tjv.domain.Order order2;

    @BeforeEach
    public void setUp() {
        String userId = "user123";
        user = new User();
        user.setUsername(userId);

        order1 = new cz.cvut.fit.tjv.domain.Order();
        order1.setId(0L);
        order1.setUser(user);
        order1.setProducts(List.of(new Product(),new Product(), new Product()));
        order1.setDate(LocalDateTime.now());
        order1.setState(OrderState.DELIVERED);
        order2= new cz.cvut.fit.tjv.domain.Order();
        order2.setId(0L);
        order2.setUser(user);
        order2.setProducts(List.of(new Product()));
        order2.setDate(LocalDateTime.now());
        order2.setState(OrderState.SENT);
    }

    @Test
    public void readAllByAuthorTest() {

        List<cz.cvut.fit.tjv.domain.Order> orders = List.of(order1, order2);

        when(orderRepository.findByUserUsername(user.getUsername())).thenReturn(orders);

        Collection<cz.cvut.fit.tjv.domain.Order> result = orderService.getAllByAuthor(user.getUsername());

        verify(orderRepository).findByUserUsername(user.getUsername());
        assertEquals(orders, result);
    }

    @Test
    public void readAllByDateTest() {
        LocalDateTime date = LocalDateTime.now();

        List<cz.cvut.fit.tjv.domain.Order> orders = new ArrayList<>(List.of(order1, order2));

        when(orderRepository.findByDateBefore(date)).thenReturn(orders);

        Collection<cz.cvut.fit.tjv.domain.Order> result = orderService.getByDateBefore(date);

        verify(orderRepository).findByDateBefore(date);
        assertEquals(orders, result);
    }

    @Test
    public void convertOrderToDtoTest() {
        cz.cvut.fit.tjv.domain.Order order = new cz.cvut.fit.tjv.domain.Order();
        Order dto = OrderServiceImpl.convertOrderToDto(order);
        assertNotNull(dto);
    }
}
