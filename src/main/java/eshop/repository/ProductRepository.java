package eshop.repository;

import eshop.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {

    @Query("select p from Product p where p.price <= :price")
    Collection<Product> findLessPrice(@Param("price") Double price);
}
