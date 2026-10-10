package eshop.controller;

import eshop.contracts.*;
import eshop.service.ProductFilter;
import eshop.service.ProductService;
import eshop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Search products",
            description = "Full-text search with category, price and stock filters. Sort: newest (default), price_asc, price_desc, name")
    public PageResponse<ProductResponse> search(@RequestParam(required = false) String q,
                                                @RequestParam(required = false) Long categoryId,
                                                @RequestParam(required = false) BigDecimal minPrice,
                                                @RequestParam(required = false) BigDecimal maxPrice,
                                                @RequestParam(defaultValue = "false") boolean inStock,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "12") int size) {
        return productService.search(
                new ProductFilter(q, categoryId, minPrice, maxPrice, inStock, false, sort), page, size);
    }

    @GetMapping("/batch")
    @Operation(summary = "Get products by ids", description = "Used by the cart to refresh prices and stock")
    public List<ProductResponse> getByIds(@RequestParam List<Long> ids) {
        return productService.getByIds(ids);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product detail")
    public ProductResponse get(@PathVariable Long id) {
        return productService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a product (admin)")
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product (admin)")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a product from the shop (admin)", description = "The product is hidden, order history is kept")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Get reviews of a product")
    public List<ReviewResponse> getReviews(@PathVariable Long id) {
        return reviewService.getByProduct(id);
    }

    @PostMapping("/{id}/reviews")
    @Operation(summary = "Add or rewrite my review of a product")
    public ReviewResponse saveReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request,
                                     Authentication authentication) {
        return reviewService.save(id, authentication.getName(), request);
    }
}
