package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.domain.Order;

import java.time.LocalDateTime;
import java.util.Collection;

public interface OrderService extends CrudService<Order, Long>{
    Collection<Order> readAllByAuthor(String userId);

    Collection<Order> readAllByDate(LocalDateTime date);
}
