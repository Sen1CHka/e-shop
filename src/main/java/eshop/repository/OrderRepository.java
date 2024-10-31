package eshop.repository;

import eshop.domain.Order;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    Collection<Order> findByUserUsername(String username);
    @Query(" SELECT o FROM Order o WHERE o.date <= :date")
    Collection<Order> findByDateBefore(@Param("date") LocalDateTime date);

}
