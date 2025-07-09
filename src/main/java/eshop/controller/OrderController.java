package eshop.controller;

import eshop.contracts.OrderResponse;
import eshop.contracts.OrderRequest;
import eshop.domain.Order;
import eshop.domain.OrderState;
import eshop.service.OrderService;
import eshop.service.OrderServiceImpl;
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
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)))
    })
    public ResponseEntity<Collection<OrderResponse>> getAllOrders(@RequestParam(required = false) String userId,
                                                                  @RequestParam(required = false) String date) throws ParseException {
        List<eshop.domain.Order> orders = StreamSupport.stream(orderService.readAll().spliterator(), false)
                .toList();

        List<OrderResponse> orderResponseDTOS = new java.util.ArrayList<>(orders.stream()
                .map(orderService::convertOrderToDto)
                .toList());

        System.out.println(date);
        if(userId!=null)
        {
            orderResponseDTOS = orderService.getAllByAuthor(userId).stream()
                    .map(orderService::convertOrderToDto)
                    .toList();
        }
        if(date!=null && !date.equals("null") && !date.isEmpty())
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTime = formatter.parse(date);
            LocalDateTime localDateTime = dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            orderResponseDTOS = orderResponseDTOS.stream()
                    .filter(orderResponse -> orderResponse.getDate().isBefore(localDateTime))
                    .toList();
        }

        return ResponseEntity.ok(orderResponseDTOS);
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new order and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful creation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)))
    })
    public ResponseEntity<Order> create(@RequestBody OrderRequest order) {
        eshop.domain.Order newOrder = orderService.convertEditToOrder(order);
        return ResponseEntity.ok(orderService.create(newOrder));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the order"),
            @ApiResponse(responseCode = "404", description = "Order not found with the given ID")
    })
    public ResponseEntity<OrderResponse> delete(@PathVariable Long id)
    {
        if(orderService.readById(id).isEmpty()) throw new RuntimeException("Order doesnt exist!");
        OrderResponse orderResponse = orderService.convertOrderToDto(orderService.readById(id).get());
        orderService.deleteById(id);
        return ResponseEntity.ok(orderResponse);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update order state", description = "Updates the state of an order based on the provided ID and new state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order state updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid state provided"),
            @ApiResponse(responseCode = "404", description = "Order not found with the given ID")
    })
    public ResponseEntity<Order> updateOrderState(@PathVariable Long id, @RequestParam Integer state) {

        try {

            Order updatedOrder = orderService.updateOrderState(id, OrderState.values()[state]);
            if (updatedOrder == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
}
