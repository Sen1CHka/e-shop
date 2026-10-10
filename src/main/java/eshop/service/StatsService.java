package eshop.service;

import eshop.contracts.OrderResponse;
import eshop.contracts.StatsResponse;
import eshop.domain.OrderState;
import eshop.repository.OrderRepository;
import eshop.repository.ProductRepository;
import eshop.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private static final int LOW_STOCK_LIMIT = 5;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public StatsService(OrderRepository orderRepository, ProductRepository productRepository,
                        UserRepository userRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    public StatsResponse getStats() {
        Map<OrderState, Long> ordersByState = new EnumMap<>(OrderState.class);
        for (OrderState state : OrderState.values()) {
            ordersByState.put(state, orderRepository.countByState(state));
        }
        return new StatsResponse(
                productRepository.countByActiveTrue(),
                userRepository.count(),
                orderRepository.count(),
                orderRepository.sumRevenue(),
                ordersByState,
                productService.toResponses(productRepository
                        .findTop8ByActiveTrueAndAvailableAmountLessThanEqualOrderByAvailableAmountAsc(LOW_STOCK_LIMIT)),
                orderRepository.findTopProducts(PageRequest.of(0, 5)).stream()
                        .map(row -> new StatsResponse.TopProduct((String) row[0], ((Number) row[1]).longValue()))
                        .toList(),
                orderRepository.findTop5ByOrderByDateDesc().stream()
                        .map(OrderResponse::from)
                        .toList()
        );
    }
}
