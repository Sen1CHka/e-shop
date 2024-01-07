package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.Order;
import cz.cvut.fit.tjv.contracts.OrderEdit;
import cz.cvut.fit.tjv.domain.OrderState;
import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.repository.OrderRepository;
import cz.cvut.fit.tjv.repository.ProductRepository;
import cz.cvut.fit.tjv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends CrudServiceImpl<cz.cvut.fit.tjv.domain.Order, Long> implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private static UserRepository userRepository;
    @Autowired
    private static ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    protected CrudRepository<cz.cvut.fit.tjv.domain.Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    public Collection<cz.cvut.fit.tjv.domain.Order> getAllByAuthor(String userId) {
        return orderRepository.findByUserUsername(userId);
    }

    @Override
    public Collection<cz.cvut.fit.tjv.domain.Order> getByDateBefore(LocalDateTime date) {
        return orderRepository.findByDateBefore(date);
    }

    public static Order convertOrderToDto(cz.cvut.fit.tjv.domain.Order order) {
        if (order == null || order.getUser() == null) return new Order();
        return new Order(
                order.getId().intValue(),
                order.getUser().getUsername(),
                order.getProducts().stream()
                        .map(ProductServiceImpl::convertProductToDto)
                        .collect(Collectors.toList()),
                order.getDate(),
                order.getState().toString(),
                order.getTotalPrice()
        );
    }

    public static cz.cvut.fit.tjv.domain.Order convertEditToOrder(OrderEdit orderDTO) {
        cz.cvut.fit.tjv.domain.Order order = new cz.cvut.fit.tjv.domain.Order();

        order.setDate(orderDTO.getDate());
        order.setState(OrderState.valueOf(orderDTO.getState()));
        User user = userRepository.findById("user123").get();
        order.setUser(user);
        Collection<Product> products = new ArrayList<>();
        products = orderDTO.getProducts().stream()
                .map(product -> productRepository.findById(product).get())
                .collect(Collectors.toList());
        order.setProducts(products);
        order.calculateTotalPrice();
        return order;
    }
    @Override
    public cz.cvut.fit.tjv.domain.Order updateOrderState(Long id, OrderState newState) {
        if(orderRepository.findById(id).isEmpty()) throw new RuntimeException("Order doesnt exist");
        cz.cvut.fit.tjv.domain.Order order = orderRepository.findById(id).get();
        order.setState(newState);
        orderRepository.save(order);
        return order;
    }


    @Override
    public cz.cvut.fit.tjv.domain.Order update(Long aLong, cz.cvut.fit.tjv.domain.Order e) {
        return null;
    }
}
