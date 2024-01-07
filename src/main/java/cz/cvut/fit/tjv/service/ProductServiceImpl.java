package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.contracts.Product;
import cz.cvut.fit.tjv.contracts.ProductEdit;
import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.repository.OrderRepository;
import cz.cvut.fit.tjv.repository.ProductRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl extends CrudServiceImpl<cz.cvut.fit.tjv.domain.Product, Long> implements ProductService{

    @Autowired
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
    public Long deleteById(Long productId) {
        Optional<cz.cvut.fit.tjv.domain.Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            cz.cvut.fit.tjv.domain.Product product = productOpt.get();
            Collection<Order> orders = product.getOrders();
            if (orders != null) {
                for (Order order : orders) {
                    order.deleteProductById(productId);
                }
            }
        }
        productRepository.deleteById(productId);
        return productId;
    }

    @Override
    public cz.cvut.fit.tjv.domain.Product update(Long id, cz.cvut.fit.tjv.domain.Product newProduct)
    {
        if(productRepository.findById(id).isEmpty()) throw new RuntimeException("Product dont found!");

        cz.cvut.fit.tjv.domain.Product product = productRepository.findById(id).get();

        if(!newProduct.getName().isEmpty())product.setName(newProduct.getName());
        if(!newProduct.getDescription().isEmpty())product.setDescription(newProduct.getDescription());
        if(!(newProduct.getPrice()==null))product.setPrice(newProduct.getPrice());
        if(!(newProduct.getAvailableAmount()==null))product.setAvailableAmount(newProduct.getAvailableAmount());
        productRepository.save(product);
        return product;
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

    public cz.cvut.fit.tjv.domain.Product convertDtoToProduct(ProductEdit product)
    {
        cz.cvut.fit.tjv.domain.Product newProduct = new cz.cvut.fit.tjv.domain.Product();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setDescription(product.getDescription());
        newProduct.setAvailableAmount(product.getAvailableAmount());
        return newProduct;
    }

}
