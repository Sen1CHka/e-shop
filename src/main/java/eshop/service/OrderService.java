package eshop.service;

import eshop.contracts.OrderItemRequest;
import eshop.contracts.OrderRequest;
import eshop.contracts.OrderResponse;
import eshop.domain.Order;
import eshop.domain.OrderItem;
import eshop.domain.OrderState;
import eshop.domain.Product;
import eshop.domain.User;
import eshop.exception.ApiException;
import eshop.repository.OrderRepository;
import eshop.repository.ProductRepository;
import eshop.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse create(String username, OrderRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        order.setState(OrderState.NEW);
        order.setDeliveryMethod(request.deliveryMethod());
        order.setPaymentMethod(request.paymentMethod());
        order.setShipping(request.shipping().toEntity());
        order.setNote(request.note() == null || request.note().isBlank() ? null : request.note().trim());

        // The same product may arrive on several lines, the stock check needs the sum.
        // Ids are processed in ascending order so concurrent checkouts lock rows in the same order.
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        for (OrderItemRequest item : request.items()) {
            quantities.merge(item.productId(), item.quantity(), Integer::sum);
        }
        quantities.keySet().stream().sorted().forEach(productId -> {
            int quantity = quantities.get(productId);
            Product product = productRepository.findByIdForUpdate(productId)
                    .filter(Product::isActive)
                    .orElseThrow(() -> ApiException.conflict("One of the products is no longer available"));
            if (product.getAvailableAmount() < quantity) {
                throw ApiException.conflict(product.getAvailableAmount() == 0
                        ? "'" + product.getName() + "' has just sold out"
                        : "Only " + product.getAvailableAmount() + " pcs of '" + product.getName() + "' are left in stock");
            }
            product.setAvailableAmount(product.getAvailableAmount() - quantity);
            order.addItem(product, quantity);
        });

        order.calculateTotalPrice();
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> getByUser(String username) {
        return orderRepository.findByUserUsernameOrderByDateDesc(username).stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse get(Long id, String username, boolean admin) {
        Order order = find(id);
        // Foreign orders are reported as missing so that ids cannot be probed
        if (!admin && !order.getUser().getUsername().equals(username)) {
            throw ApiException.notFound("Order not found");
        }
        return OrderResponse.from(order);
    }

    public List<OrderResponse> search(OrderState state, String username, LocalDate dateTo) {
        Specification<Order> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (state != null) {
                predicates.add(cb.equal(root.get("state"), state));
            }
            if (username != null && !username.isBlank()) {
                String like = "%" + username.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("user").get("username")), like),
                        cb.like(cb.lower(root.get("user").get("realName")), like)));
            }
            if (dateTo != null) {
                predicates.add(cb.lessThan(root.get("date"), dateTo.plusDays(1).atStartOfDay()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return orderRepository.findAll(specification, Sort.by("date").descending()).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse cancel(Long id, String username) {
        Order order = find(id);
        if (!order.getUser().getUsername().equals(username)) {
            throw ApiException.notFound("Order not found");
        }
        if (order.getState() != OrderState.NEW && order.getState() != OrderState.PROCESSING) {
            throw ApiException.conflict("The order can no longer be cancelled");
        }
        return changeState(order, OrderState.CANCELLED);
    }

    @Transactional
    public OrderResponse updateState(Long id, OrderState newState) {
        Order order = find(id);
        if (order.getState() == newState) {
            return OrderResponse.from(order);
        }
        if (order.getState().isFinal()) {
            throw ApiException.conflict("A " + order.getState().name().toLowerCase() + " order cannot be changed");
        }
        return changeState(order, newState);
    }

    private OrderResponse changeState(Order order, OrderState newState) {
        if (newState == OrderState.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setAvailableAmount(product.getAvailableAmount() + item.getQuantity());
            }
        }
        order.setState(newState);
        return OrderResponse.from(order);
    }

    private Order find(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Order not found"));
    }
}
