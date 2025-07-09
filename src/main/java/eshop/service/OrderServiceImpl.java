package eshop.service;

import eshop.contracts.OrderResponse;
import eshop.contracts.OrderRequest;
import eshop.domain.Order;
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

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
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

    public OrderResponse convertOrderToDto(Order order) {
        if (order == null || order.getUser() == null) return new OrderResponse();
        Integer orderState = 0;
        for (OrderState day : OrderState.values()) {
            if (day.name().equalsIgnoreCase(order.getState().toString())) {
                orderState = day.ordinal();
            }
        }

        return new OrderResponse(
                order.getId().intValue(),
                order.getUser().getUsername(),
                order.getProducts().stream()
                        .map(productService::convertProductToDto)
                        .collect(Collectors.toList()),
                order.getDate(),
                order.getState().toString(),
                orderState,
                order.getTotalPrice()
        );
    }

    public Order convertEditToOrder(OrderRequest orderDTO) {
        Order order = new eshop.domain.Order();
        order.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        order.setState(OrderState.values()[orderDTO.getState()]);
        Optional<User> optionalUser = userRepository.findByUsername(orderDTO.getUsername());
        if(optionalUser.isEmpty())
        {
            return null;
        }
        order.setUser(optionalUser.get());
        Collection<Product> products = orderDTO.getProducts().stream()
                .map(product -> productRepository.findById(product).get())
                .collect(Collectors.toList());
        order.setProducts(products);
        order.calculateTotalPrice();

        return order;
    }
    @Override
    public Order updateOrderState(Long id, OrderState newState) {
        if(orderRepository.findById(id).isEmpty()) throw new RuntimeException("Order doesnt exist");
        eshop.domain.Order order = orderRepository.findById(id).get();
        order.setState(newState);
        orderRepository.save(order);
        return order;
    }


    @Override
    public Order update(Long aLong, Order e) {
        return null;
    }
}
