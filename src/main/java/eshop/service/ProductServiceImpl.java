package eshop.service;

import eshop.contracts.ProductResponse;
import eshop.contracts.ProductRequest;
import eshop.domain.Order;
import eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Service
public class ProductServiceImpl extends CrudServiceImpl<eshop.domain.Product, Long> implements ProductService{

    @Autowired
    private static ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected CrudRepository<eshop.domain.Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    public Collection<eshop.domain.Product> getLessPrice(Double price) {
        return productRepository.findLessPrice(price);
    }

    @Override
    public Long deleteById(Long productId) {
        Optional<eshop.domain.Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            eshop.domain.Product product = productOpt.get();
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
    public eshop.domain.Product update(Long id, eshop.domain.Product newProduct)
    {
        if(productRepository.findById(id).isEmpty()) throw new RuntimeException("Product dont found!");

        eshop.domain.Product product = productRepository.findById(id).get();

        if(!newProduct.getName().isEmpty())product.setName(newProduct.getName());
        if(!newProduct.getDescription().isEmpty())product.setDescription(newProduct.getDescription());
        if(!(newProduct.getPrice()==null))product.setPrice(newProduct.getPrice());
        if(!(newProduct.getAvailableAmount()==null))product.setAvailableAmount(newProduct.getAvailableAmount());
        productRepository.save(product);
        return product;
    }


    public static ProductResponse convertProductToDto(eshop.domain.Product product)
    {
        if(product==null) return new ProductResponse();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableAmount()
        );
    }

    public eshop.domain.Product convertDtoToProduct(ProductRequest product)
    {
        eshop.domain.Product newProduct = new eshop.domain.Product();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setDescription(product.getDescription());
        newProduct.setAvailableAmount(product.getAvailableAmount());
        return newProduct;
    }

}
