package cz.cvut.fit.tjv.repositary;

import cz.cvut.fit.tjv.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {
    Collection<Product> findByPrice(Double price);

    @Query("SELECT p FROM Product p WHERE p.price > (SELECT AVG(price) FROM Product )")
    Collection<Product> findExpensiveThanAverage();
}
