package cz.cvut.fit.tjv.service;
import cz.cvut.fit.tjv.domain.Product;

import java.util.Collection;


public interface ProductService extends CrudService<Product, Long>{
    Collection<Product> findByPrice(Double price);
    Collection<Product> findExpensiveThanAverage();
}
