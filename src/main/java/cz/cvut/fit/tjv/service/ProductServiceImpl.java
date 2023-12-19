package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.repositary.ProductRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
public class ProductServiceImpl extends CrudServiceImpl<Product, Long> implements ProductService{
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected CrudRepository<Product, Long> getRepository() {
        return productRepository;
    }
}
