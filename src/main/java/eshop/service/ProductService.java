package eshop.service;
import eshop.contracts.ProductEdit;
import eshop.domain.Product;

import java.util.Collection;


public interface ProductService extends CrudService<Product, Long>{
    Collection<Product> getLessPrice(Double price);
    Product convertDtoToProduct(ProductEdit product);

}
