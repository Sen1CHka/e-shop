package eshop.repository;

import eshop.domain.Order;
import eshop.domain.OrderState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByUserUsernameOrderByDateDesc(String username);

    @Query("select o from Order o where o.date <= :date")
    Collection<Order> findByDateBefore(@Param("date") LocalDateTime date);

    List<Order> findTop5ByOrderByDateDesc();

    long countByState(OrderState state);

    void deleteByUserId(Long userId);

    @Query("select coalesce(sum(o.totalPrice), 0) from Order o where o.state <> eshop.domain.OrderState.CANCELLED")
    BigDecimal sumRevenue();

    @Query("""
            select i.productName, sum(i.quantity) from OrderItem i
            where i.order.state <> eshop.domain.OrderState.CANCELLED
            group by i.productName
            order by sum(i.quantity) desc
            """)
    List<Object[]> findTopProducts(Pageable pageable);
}
