package eshop.controller;

import eshop.contracts.AddressRequest;
import eshop.contracts.AddressResponse;
import eshop.contracts.ProductResponse;
import eshop.service.AddressService;
import eshop.service.ProductService;
import eshop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Customer account", description = "Favourites, address book and own reviews")
public class AccountController {

    private final ProductService productService;
    private final AddressService addressService;
    private final ReviewService reviewService;

    public AccountController(ProductService productService, AddressService addressService, ReviewService reviewService) {
        this.productService = productService;
        this.addressService = addressService;
        this.reviewService = reviewService;
    }

    @GetMapping("/favorites")
    @Operation(summary = "Get my favourite products")
    public List<ProductResponse> getFavorites(Authentication authentication) {
        return productService.getFavorites(authentication.getName());
    }

    @PutMapping("/favorites/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add a product to my favourites")
    public void addFavorite(@PathVariable Long productId, Authentication authentication) {
        productService.addFavorite(authentication.getName(), productId);
    }

    @DeleteMapping("/favorites/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a product from my favourites")
    public void removeFavorite(@PathVariable Long productId, Authentication authentication) {
        productService.removeFavorite(authentication.getName(), productId);
    }

    @GetMapping("/addresses")
    @Operation(summary = "Get my saved addresses")
    public List<AddressResponse> getAddresses(Authentication authentication) {
        return addressService.getByUser(authentication.getName());
    }

    @PostMapping("/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a new address")
    public AddressResponse createAddress(@Valid @RequestBody AddressRequest request, Authentication authentication) {
        return addressService.create(authentication.getName(), request);
    }

    @PutMapping("/addresses/{id}")
    @Operation(summary = "Update my address")
    public AddressResponse updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request,
                                         Authentication authentication) {
        return addressService.update(id, authentication.getName(), request);
    }

    @DeleteMapping("/addresses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete my address")
    public void deleteAddress(@PathVariable Long id, Authentication authentication) {
        addressService.delete(id, authentication.getName());
    }

    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a review", description = "Own review, or any review for an administrator")
    public void deleteReview(@PathVariable Long id, Authentication authentication) {
        reviewService.delete(id, authentication.getName(), Roles.isAdmin(authentication));
    }
}
