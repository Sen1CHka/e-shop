package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.service.ProductService;
import cz.cvut.fit.tjv.service.ProductServiceImpl;
import cz.cvut.fit.tjv.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<Product> readAll() {
        return productService.readAll();
    }

}
