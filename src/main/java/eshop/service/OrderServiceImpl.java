package eshop.service;

import eshop.contracts.Order;
import eshop.contracts.OrderEdit;
import eshop.domain.OrderState;
import eshop.domain.Product;
import eshop.domain.User;
import eshop.repository.OrderRepository;
import eshop.repository.ProductRepository;
import eshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends CrudServiceImpl<eshop.domain.Order, Long> implements OrderService {

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
    protected CrudRepository<eshop.domain.Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    public Collection<eshop.domain.Order> getAllByAuthor(String userId) {
        return orderRepository.findByUserUsername(userId);
    }

    @Override
    public Collection<eshop.domain.Order> getByDateBefore(LocalDateTime date) {
        return orderRepository.findByDateBefore(date);
    }

    public static Order convertOrderToDto(eshop.domain.Order order) {
        if (order == null || order.getUser() == null) return new Order();
        Integer orderState = 0;
        for (OrderState day : OrderState.values()) {
            if (day.name().equalsIgnoreCase(order.getState().toString())) {
                orderState = day.ordinal();
            }
        }

        return new Order(
                order.getId().intValue(),
                order.getUser().getUsername(),
                order.getProducts().stream()
                        .map(ProductServiceImpl::convertProductToDto)
                        .collect(Collectors.toList()),
                order.getDate(),
                order.getState().toString(),
                orderState,
                order.getTotalPrice()
        );
    }

    public static eshop.domain.Order convertEditToOrder(OrderEdit orderDTO) {
        eshop.domain.Order order = new eshop.domain.Order();

        order.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        order.setState(OrderState.values()[orderDTO.getState()]);
        Optional<User> optionalUser = userRepository.findById("user123");
        User user;
        if(optionalUser.isEmpty())
        {
            user = new User();
            user.setUsername("user123");
            user.setRealName("Default User");
            user.setEmail("defaultuser@fit.cz");
            user.setPassword("password123");

            userRepository.save(user);
        }
        else {
            user = optionalUser.get();
        }
        order.setUser(user);
        Collection<Product> products = new ArrayList<>();
        products = orderDTO.getProducts().stream()
                .map(product -> productRepository.findById(product).get())
                .collect(Collectors.toList());
        order.setProducts(products);
        order.calculateTotalPrice();

        System.out.println(order);

        return order;
    }
    @Override
    public eshop.domain.Order updateOrderState(Long id, OrderState newState) {
        if(orderRepository.findById(id).isEmpty()) throw new RuntimeException("Order doesnt exist");
        eshop.domain.Order order = orderRepository.findById(id).get();
        order.setState(newState);
        orderRepository.save(order);
        return order;
    }


    @Override
    public eshop.domain.Order update(Long aLong, eshop.domain.Order e) {
        return null;
    }
}
