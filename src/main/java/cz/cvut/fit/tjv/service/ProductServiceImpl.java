package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.ProductDTO;
import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.repositary.ProductRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;


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

    @Override
    public Collection<Product> findByPrice(Double price) {
        return productRepository.findByPrice(price);
    }

    @Override
    public Collection<Product> findExpensiveThanAverage() {
        return productRepository.findExpensiveThanAverage();
    }

    public static ProductDTO convertProductToDto(Product product)
    {
        if(product==null) return new ProductDTO();
        return new ProductDTO(
                product.getId().intValue(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableAmount().intValue()
        );
    }
}
