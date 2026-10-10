package eshop.service;

import eshop.contracts.PageResponse;
import eshop.contracts.ProductRequest;
import eshop.contracts.ProductResponse;
import eshop.domain.Product;
import eshop.domain.User;
import eshop.exception.ApiException;
import eshop.repository.CategoryRepository;
import eshop.repository.ProductRepository;
import eshop.repository.ReviewRepository;
import eshop.repository.ReviewRepository.RatingStats;
import eshop.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private static final int MAX_PAGE_SIZE = 100;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
                          ReviewRepository reviewRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public PageResponse<ProductResponse> search(ProductFilter filter, int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                Math.max(page, 0), Math.min(Math.max(size, 1), MAX_PAGE_SIZE), toSort(filter.sort()));
        Page<Product> result = productRepository.findAll(toSpecification(filter), pageRequest);
        return new PageResponse<>(toResponses(result.getContent()), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    public ProductResponse get(Long id) {
        return toResponses(List.of(find(id))).get(0);
    }

    /**
     * Returns the products in the order of the given ids; unknown ids are skipped.
     */
    public List<ProductResponse> getByIds(Collection<Long> ids) {
        Map<Long, ProductResponse> byId = toResponses(productRepository.findAllById(ids)).stream()
                .collect(Collectors.toMap(ProductResponse::id, Function.identity()));
        return ids.stream().map(byId::get).filter(Objects::nonNull).toList();
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        productRepository.save(product);
        return ProductResponse.from(product, 0, 0);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = find(id);
        apply(product, request);
        return toResponses(List.of(product)).get(0);
    }

    /**
     * Products are only hidden from the shop, old orders still reference them.
     */
    @Transactional
    public void delete(Long id) {
        Product product = find(id);
        product.setActive(false);
        userRepository.removeFavoriteFromAllUsers(id);
    }

    public List<ProductResponse> getFavorites(String username) {
        List<Product> favorites = findUser(username).getFavorites().stream()
                .filter(Product::isActive)
                .sorted(Comparator.comparing(Product::getName))
                .toList();
        return toResponses(favorites);
    }

    @Transactional
    public void addFavorite(String username, Long productId) {
        findUser(username).getFavorites().add(find(productId));
    }

    @Transactional
    public void removeFavorite(String username, Long productId) {
        findUser(username).getFavorites().removeIf(product -> product.getId().equals(productId));
    }

    public List<ProductResponse> toResponses(List<Product> products) {
        if (products.isEmpty()) return List.of();
        Map<Long, RatingStats> stats = reviewRepository
                .ratingStats(products.stream().map(Product::getId).toList()).stream()
                .collect(Collectors.toMap(RatingStats::getProductId, Function.identity()));
        return products.stream()
                .map(product -> {
                    RatingStats rating = stats.get(product.getId());
                    return rating == null
                            ? ProductResponse.from(product, 0, 0)
                            : ProductResponse.from(product, rating.getAverage(), rating.getCount());
                })
                .toList();
    }

    private Product find(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Product not found"));
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User not found"));
    }

    private void apply(Product product, ProductRequest request) {
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setAvailableAmount(request.availableAmount());
        product.setBrand(blankToNull(request.brand()));
        product.setImageUrl(blankToNull(request.imageUrl()));
        product.setCategory(request.categoryId() == null ? null : categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> ApiException.badRequest("Category does not exist")));
        if (request.active() != null) {
            product.setActive(request.active());
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static Specification<Product> toSpecification(ProductFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!filter.includeInactive()) {
                predicates.add(cb.isTrue(root.get("active")));
            }
            if (filter.query() != null && !filter.query().isBlank()) {
                String like = "%" + filter.query().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("brand")), like),
                        cb.like(cb.lower(root.get("description")), like)));
            }
            if (filter.categoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.categoryId()));
            }
            if (filter.minPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.minPrice()));
            }
            if (filter.maxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.maxPrice()));
            }
            if (filter.inStockOnly()) {
                predicates.add(cb.greaterThan(root.get("availableAmount"), 0));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Sort toSort(String sort) {
        Sort primary = switch (sort == null ? "" : sort) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "name" -> Sort.by("name").ascending();
            default -> Sort.by("createdAt").descending();
        };
        return primary.and(Sort.by("id").descending());
    }
}
