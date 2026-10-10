package eshop.controller;

import eshop.contracts.CheckoutOptionsResponse;
import eshop.contracts.OrderRequest;
import eshop.contracts.OrderResponse;
import eshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/checkout/options")
    @Operation(summary = "Get delivery and payment methods with their prices")
    public CheckoutOptionsResponse getCheckoutOptions() {
        return CheckoutOptionsResponse.all();
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order", description = "Checks stock, reserves the goods and computes the price on the server")
    public OrderResponse create(@Valid @RequestBody OrderRequest request, Authentication authentication) {
        return orderService.create(authentication.getName(), request);
    }

    @GetMapping("/orders")
    @Operation(summary = "Get my orders")
    public List<OrderResponse> getMine(Authentication authentication) {
        return orderService.getByUser(authentication.getName());
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Get order detail", description = "Customers see only their own orders")
    public OrderResponse get(@PathVariable Long id, Authentication authentication) {
        return orderService.get(id, authentication.getName(), Roles.isAdmin(authentication));
    }

    @PostMapping("/orders/{id}/cancel")
    @Operation(summary = "Cancel my order", description = "Possible until the order is shipped; the goods return to stock")
    public OrderResponse cancel(@PathVariable Long id, Authentication authentication) {
        return orderService.cancel(id, authentication.getName());
    }
}
