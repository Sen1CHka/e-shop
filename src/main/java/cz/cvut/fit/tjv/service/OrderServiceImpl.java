package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.repositary.OrderRepository;
import cz.cvut.fit.tjv.repositary.ProductRepository;
import cz.cvut.fit.tjv.repositary.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrderServiceImpl extends CrudServiceImpl<Order, Long> implements OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    protected CrudRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    public Collection<Order> readAllByAuthor(String userId) {
        return orderRepository.findByClientUsername(userId);
    }
}
