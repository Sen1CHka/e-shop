package cz.cvut.fit.tjv.repositary;

import cz.cvut.fit.tjv.domain.Order;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    Collection<Order> findByClientUsername(String userId);
    Collection<Order> findByData(LocalDateTime data);
}
