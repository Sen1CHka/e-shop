package cz.cvut.fit.tjv.controller;

import cz.cvut.fit.tjv.contracts.Order;
import cz.cvut.fit.tjv.contracts.Product;
import cz.cvut.fit.tjv.contracts.ProductEdit;
import cz.cvut.fit.tjv.service.OrderServiceImpl;
import cz.cvut.fit.tjv.service.ProductService;
import cz.cvut.fit.tjv.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public ResponseEntity<Collection<Product>> getAllProducts() {
        List<cz.cvut.fit.tjv.domain.Product> orders = StreamSupport.stream(productService.readAll().spliterator(), false)
                .toList();

        List<Product> productDTOs = orders.stream()
                .map(ProductServiceImpl::convertProductToDto)
                .toList();
        return ResponseEntity.ok(productDTOs);
    }
    @PostMapping
    public ResponseEntity<cz.cvut.fit.tjv.domain.Product> create(@RequestBody ProductEdit product) {
        cz.cvut.fit.tjv.domain.Product newProduct = ProductServiceImpl.convertDtoToProduct(product);
        return ResponseEntity.ok(productService.create(newProduct));
    }

    @PutMapping("/update")
    public cz.cvut.fit.tjv.domain.Product update(@RequestBody cz.cvut.fit.tjv.contracts.Product newProduct, @PathVariable Long id)
    {
        //UPDATE
       return null;
    }

    @GetMapping("/lesspr")
    public ResponseEntity<Collection<cz.cvut.fit.tjv.contracts.Product>> readAllByLessPrice(@RequestParam Double price)
    {
        return ResponseEntity.ok(productService.getLessPrice(price).stream()
                .map(ProductServiceImpl::convertProductToDto)
                .toList());
    }

    @GetMapping("/expaver")
    public ResponseEntity<Collection<cz.cvut.fit.tjv.contracts.Product>> readAllExpensiveThanAverage()
    {
        return ResponseEntity.ok(productService.getExpensiveThanAverage().stream()
                .map(ProductServiceImpl::convertProductToDto)
                .collect(Collectors.toList()));
    }
}
