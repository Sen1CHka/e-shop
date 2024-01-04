package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.contracts.Order;
import cz.cvut.fit.tjv.contracts.OrderEdit;
import cz.cvut.fit.tjv.service.OrderService;
import cz.cvut.fit.tjv.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
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
    public ResponseEntity<Collection<Order>> getAllOrders() {
        List<cz.cvut.fit.tjv.domain.Order> orders = StreamSupport.stream(orderService.readAll().spliterator(), false)
                .toList();

        List<Order> orderDTOs = orders.stream()
                .map(OrderServiceImpl::convertOrderToDto)
                .toList();
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/byauthor")
    public ResponseEntity<Collection<cz.cvut.fit.tjv.contracts.Order>> readAllByAuthor(@RequestParam String userId) {
        return ResponseEntity.ok(orderService.getAllByAuthor(userId).stream()
                .map(OrderServiceImpl::convertOrderToDto)
                .toList());
    }

    @GetMapping("/bydate")
    public ResponseEntity<Collection<cz.cvut.fit.tjv.contracts.Order>> readAllByDate(@RequestParam String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = formatter.parse(date);
        LocalDateTime localDateTime = dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(dateTime);
        System.out.println(localDateTime);
        return ResponseEntity.ok(orderService.getByDateBefore(localDateTime).stream()
                .map(OrderServiceImpl::convertOrderToDto)
                .toList());
    }

    @PostMapping
    public ResponseEntity<cz.cvut.fit.tjv.domain.Order> create(@RequestBody OrderEdit order) {
        cz.cvut.fit.tjv.domain.Order newOrder = OrderServiceImpl.convertEditToOrder(order);
        return ResponseEntity.ok(orderService.create(newOrder));
    }




}
