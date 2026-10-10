package eshop.repository;

import eshop.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where p.price <= :price")
    Collection<Product> findLessPrice(@Param("price") BigDecimal price);

    // Locks the row so two concurrent checkouts cannot sell the same last piece
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    long countByCategoryIdAndActiveTrue(Long categoryId);

    long countByCategoryId(Long categoryId);

    long countByActiveTrue();

    List<Product> findTop8ByActiveTrueAndAvailableAmountLessThanEqualOrderByAvailableAmountAsc(Integer amount);
}
