package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.Product;
import cz.cvut.fit.tjv.contracts.ProductEdit;
import cz.cvut.fit.tjv.repository.ProductRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class ProductServiceImpl extends CrudServiceImpl<cz.cvut.fit.tjv.domain.Product, Long> implements ProductService{
    private static ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected CrudRepository<cz.cvut.fit.tjv.domain.Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    public Collection<cz.cvut.fit.tjv.domain.Product> getLessPrice(Double price) {
        return productRepository.findLessPrice(price);
    }

    @Override
    public Collection<cz.cvut.fit.tjv.domain.Product> getExpensiveThanAverage() {
        return productRepository.findExpensiveThanAverage();
    }

    public static Product convertProductToDto(cz.cvut.fit.tjv.domain.Product product)
    {
        if(product==null) return new Product();
        return new Product(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableAmount()
        );
    }

    public static cz.cvut.fit.tjv.domain.Product getProductById(Long id)
    {
        return productRepository.findById(id).get();
    }

    public static cz.cvut.fit.tjv.domain.Product convertDtoToProduct(ProductEdit product)
    {
        cz.cvut.fit.tjv.domain.Product newProduct = new cz.cvut.fit.tjv.domain.Product();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setDescription(product.getDescription());
        newProduct.setAvailableAmount(product.getAvailableAmount());
        return newProduct;
    }

}
