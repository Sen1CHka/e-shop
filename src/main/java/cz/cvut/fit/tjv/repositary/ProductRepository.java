package cz.cvut.fit.tjv.repositary;

import cz.cvut.fit.tjv.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {
}
