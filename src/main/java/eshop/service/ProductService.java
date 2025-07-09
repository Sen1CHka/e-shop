package eshop.service;
import eshop.contracts.ProductRequest;
import eshop.contracts.ProductResponse;
import eshop.domain.Product;

import java.util.Collection;


public interface ProductService extends CrudService<Product, Long>{
    Collection<Product> getLessPrice(Double price);
    Product convertDtoToProduct(ProductRequest product);
    ProductResponse convertProductToDto(Product product);
}
