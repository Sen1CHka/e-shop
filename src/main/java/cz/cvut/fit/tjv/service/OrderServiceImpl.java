package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.OrderDTO;
import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.repositary.OrderRepository;
import cz.cvut.fit.tjv.repositary.ProductRepository;
import cz.cvut.fit.tjv.repositary.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

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
        return orderRepository.findByUserUsername(userId);
    }

    @Override
    public Collection<Order> readAllByDate(LocalDateTime date) {
        return orderRepository.findByDate(date);
    }

    public static OrderDTO convertOrderToDto(Order order) {
        if (order == null || order.getUser() == null) return new OrderDTO();
        return new OrderDTO(
                order.getId().intValue(),
                order.getUser().getUsername(),
                order.getProducts().stream()
                        .map(ProductServiceImpl::convertProductToDto)
                        .collect(Collectors.toList()),
                order.getDate(),
                order.getState().toString()
        );
    }
}
