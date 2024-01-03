package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.service.ProductService;
import cz.cvut.fit.tjv.service.ProductServiceImpl;
import cz.cvut.fit.tjv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping("/api/product")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<Product> readAll() {
        return productService.readAll();
    }
    @PostMapping("/create")
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/update")
    public Product update(@RequestBody Product newProduct, @PathVariable Long id)
    {
        return productService.readById(id).map(prod -> {
            prod.setName(newProduct.getName());
            prod.setDescription(newProduct.getDescription());
            prod.setPrice(prod.getPrice());
            prod.setAvailableAmount(newProduct.getAvailableAmount());
            return productService.save(newProduct);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return productService.save(newProduct);
        });
    }

    @GetMapping("/lesspr")
    public Collection<Product> readAllByLessPrice(@RequestParam Double price)
    {
        return productService.findByPrice(price);
    }

    @GetMapping("/expaver")
    public Collection<Product> readAllExpensiveThanAverage()
    {
        return productService.findExpensiveThanAverage();
    }
}
