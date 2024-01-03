package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.contracts.OrderDTO;
import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.service.OrderService;
import cz.cvut.fit.tjv.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = StreamSupport.stream(orderService.readAll().spliterator(), false)
                .toList();

        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderServiceImpl::convertOrderToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/byAuthor")
    public Collection<Order> readAllByAuthor(@RequestParam String userId) {
        return orderService.readAllByAuthor(userId);
    }

    @GetMapping("/byDate")
    public Collection<Order> readAllByDate(@RequestParam LocalDateTime date) {
        return orderService.readAllByDate(date);
    }

    @PostMapping("/create")
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }


}
