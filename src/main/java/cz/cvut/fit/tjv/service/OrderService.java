package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.domain.OrderState;

import java.time.LocalDateTime;
import java.util.Collection;

public interface OrderService extends CrudService<Order, Long>{
    Collection<Order> getAllByAuthor(String userId);
    Collection<Order> getByDateBefore(LocalDateTime date);
    Order updateOrderState(Long id, OrderState newState);
}
