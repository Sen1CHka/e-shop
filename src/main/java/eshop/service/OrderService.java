package eshop.service;

import eshop.domain.Order;
import eshop.domain.OrderState;

import java.time.LocalDateTime;
import java.util.Collection;

public interface OrderService extends CrudService<Order, Long>{
    Collection<Order> getAllByAuthor(String userId);
    Collection<Order> getByDateBefore(LocalDateTime date);
    Order updateOrderState(Long id, OrderState newState);
}
