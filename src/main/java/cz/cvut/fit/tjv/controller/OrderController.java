package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.contracts.Order;
import cz.cvut.fit.tjv.contracts.OrderEdit;
import cz.cvut.fit.tjv.domain.OrderState;
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
    public ResponseEntity<Collection<Order>> getAllOrders(@RequestParam(required = false) String userId,
                                                          @RequestParam(required = false) String date) throws ParseException {
        List<cz.cvut.fit.tjv.domain.Order> orders = StreamSupport.stream(orderService.readAll().spliterator(), false)
                .toList();

        List<Order> orderDTOs = new java.util.ArrayList<>(orders.stream()
                .map(OrderServiceImpl::convertOrderToDto)
                .toList());

        if(!(userId==null))
        {
            orderDTOs = orderService.getAllByAuthor(userId).stream()
                    .map(OrderServiceImpl::convertOrderToDto)
                    .toList();
        }
        if(!(date==null))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTime = formatter.parse(date);
            LocalDateTime localDateTime = dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            orderDTOs = orderDTOs.stream()
                    .filter(order -> order.getDate().isBefore(localDateTime))
                    .toList();
        }

        return ResponseEntity.ok(orderDTOs);
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
        if(orderService.readById(id).isEmpty()) throw new RuntimeException("Order doesnt exist!");
        Order order = OrderServiceImpl.convertOrderToDto(orderService.readById(id).get());
        orderService.deleteById(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update order state", description = "Updates the state of an order based on the provided ID and new state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order state updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid state provided"),
            @ApiResponse(responseCode = "404", description = "Order not found with the given ID")
    })
    public ResponseEntity<?> updateOrderState(@PathVariable Long id, @RequestParam Integer state) {

        try {

            cz.cvut.fit.tjv.domain.Order updatedOrder = orderService.updateOrderState(id, OrderState.values()[state]);
            if (updatedOrder == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
}
