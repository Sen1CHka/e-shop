package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.contracts.Order;
import cz.cvut.fit.tjv.contracts.OrderEdit;
import cz.cvut.fit.tjv.service.OrderService;
import cz.cvut.fit.tjv.service.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class)))
    })
    public ResponseEntity<Collection<Order>> getAllOrders() {
        List<cz.cvut.fit.tjv.domain.Order> orders = StreamSupport.stream(orderService.readAll().spliterator(), false)
                .toList();

        List<Order> orderDTOs = orders.stream()
                .map(OrderServiceImpl::convertOrderToDto)
                .toList();
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/byauthor")
    @Operation(summary = "Get orders by author", description = "Returns a list of orders filtered by author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class)))
    })
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
    @Operation(summary = "Create a new order", description = "Creates a new order and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful creation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class)))
    })
    public ResponseEntity<cz.cvut.fit.tjv.domain.Order> create(@RequestBody OrderEdit order) {
        cz.cvut.fit.tjv.domain.Order newOrder = OrderServiceImpl.convertEditToOrder(order);
        return ResponseEntity.ok(orderService.create(newOrder));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the order"),
            @ApiResponse(responseCode = "404", description = "Order not found with the given ID")
    })
    public ResponseEntity<?> delete(@PathVariable Long id)
    {
        orderService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
