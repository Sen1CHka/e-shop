package eshop.controller;

import eshop.contracts.ProductResponse;
import eshop.contracts.ProductRequest;
import eshop.service.ProductService;
import eshop.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class)))
    })
    public ResponseEntity<?> getAllProducts(@RequestParam(required = false) String price) {
        List<eshop.domain.Product> orders = StreamSupport.stream(productService.readAll().spliterator(), false)
                .toList();

        List<ProductResponse> productResponseDTOS = new java.util.ArrayList<>(orders.stream()
                .map(productService::convertProductToDto)
                .sorted(Comparator.comparing(ProductResponse::getId))
                .toList());
        if(price!=null && !price.equals("null"))
        {
            productResponseDTOS = productService.getLessPrice(Double.valueOf(price)).stream()
                .map(productService::convertProductToDto)
                .toList();
        }
        return ResponseEntity.ok(productResponseDTOS);
    }
    @PostMapping
    @Operation(summary = "Create a product", description = "Creates a new product with the given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful creation of the product",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class)))
    })
    public ResponseEntity<?> create(@RequestBody ProductRequest product) {
        eshop.domain.Product newProduct = productService.convertDtoToProduct(product);
        return ResponseEntity.ok(productService.create(newProduct));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates the product details for the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> update(@RequestBody ProductRequest newProduct, @PathVariable Long id)
    {
        if(!productService.readById(id).isPresent())
        {
            ResponseEntity.notFound().build();
        }

        eshop.domain.Product product = productService.readById(id).get();

       return ResponseEntity.ok( productService.update(id, productService.convertDtoToProduct(newProduct)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> delete(@PathVariable Long id)
    {
        return ResponseEntity.ok(productService.deleteById(id));
    }

}
