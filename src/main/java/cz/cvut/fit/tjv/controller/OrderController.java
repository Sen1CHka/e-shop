package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Order> readAll() {
        return orderService.readAll();
    }
    @GetMapping("/byAuthor")
    public Collection<Order> readAllByAuthor(@RequestParam String userId) {
        return orderService.readAllByAuthor(userId);
    }

    @GetMapping("/byDate")
    public Collection<Order> readAllByDate(@RequestParam LocalDateTime date) {
        return orderService.readAllByDate(date);
    }
}
