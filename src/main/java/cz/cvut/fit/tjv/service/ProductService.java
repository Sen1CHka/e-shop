package cz.cvut.fit.tjv.service;
import cz.cvut.fit.tjv.contracts.ProductEdit;
import cz.cvut.fit.tjv.domain.Product;

import java.util.Collection;


public interface ProductService extends CrudService<Product, Long>{
    Collection<Product> getLessPrice(Double price);
    cz.cvut.fit.tjv.domain.Product convertDtoToProduct(ProductEdit product);

}
