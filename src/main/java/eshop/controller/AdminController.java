package eshop.controller;

import eshop.contracts.*;
import eshop.domain.OrderState;
import eshop.service.OrderService;
import eshop.service.ProductFilter;
import eshop.service.ProductService;
import eshop.service.StatsService;
import eshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administration", description = "Available to users with the ADMIN role only")
public class AdminController {

    private final StatsService statsService;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    public AdminController(StatsService statsService, OrderService orderService,
                           ProductService productService, UserService userService) {
        this.statsService = statsService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public StatsResponse getStats() {
        return statsService.getStats();
    }

    @GetMapping("/products")
    @Operation(summary = "Search products including the hidden ones")
    public PageResponse<ProductResponse> searchProducts(@RequestParam(required = false) String q,
                                                        @RequestParam(required = false) Long categoryId,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        return productService.search(new ProductFilter(q, categoryId, null, null, false, true, sort), page, size);
    }

    @GetMapping("/orders")
    @Operation(summary = "Search all orders", description = "Filter by state, customer and date (orders created up to the given day)")
    public List<OrderResponse> searchOrders(@RequestParam(required = false) OrderState state,
                                            @RequestParam(required = false) String user,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        return orderService.search(state, user, dateTo);
    }

    @PatchMapping("/orders/{id}/state")
    @Operation(summary = "Change order state", description = "Cancelling returns the goods to stock; delivered and cancelled orders are final")
    public OrderResponse updateOrderState(@PathVariable Long id, @Valid @RequestBody OrderStateRequest request) {
        return orderService.updateState(id, request.state());
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public List<UserResponse> getUsers() {
        return userService.findAll();
    }

    @PatchMapping("/users/{id}/role")
    @Operation(summary = "Change user role")
    public UserResponse changeRole(@PathVariable Long id, @Valid @RequestBody RoleRequest request,
                                   Authentication authentication) {
        return userService.changeRole(id, request.role(), authentication.getName());
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user with their orders, reviews and addresses")
    public void deleteUser(@PathVariable Long id, Authentication authentication) {
        userService.delete(id, authentication.getName());
    }
}
