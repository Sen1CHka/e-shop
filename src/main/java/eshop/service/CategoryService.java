package eshop.service;

import eshop.contracts.CategoryRequest;
import eshop.contracts.CategoryResponse;
import eshop.domain.Category;
import eshop.exception.ApiException;
import eshop.repository.CategoryRepository;
import eshop.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll(Sort.by("name")).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String slug = slugify(request.name());
        if (categoryRepository.existsBySlug(slug)) {
            throw ApiException.conflict("Category with this name already exists");
        }
        Category category = new Category(request.name().trim(), slug, request.description(), request.icon());
        categoryRepository.save(category);
        return toResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = find(id);
        String slug = slugify(request.name());
        if (categoryRepository.existsBySlugAndIdNot(slug, id)) {
            throw ApiException.conflict("Category with this name already exists");
        }
        category.setName(request.name().trim());
        category.setSlug(slug);
        category.setDescription(request.description());
        category.setIcon(request.icon());
        return toResponse(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = find(id);
        long products = productRepository.countByCategoryId(id);
        if (products > 0) {
            throw ApiException.conflict("Category still contains " + products
                    + " product(s). Move them to another category first.");
        }
        categoryRepository.delete(category);
    }

    private Category find(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Category not found"));
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.from(category, productRepository.countByCategoryIdAndActiveTrue(category.getId()));
    }

    static String slugify(String name) {
        String slug = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (slug.isEmpty()) {
            throw ApiException.badRequest("Category name must contain letters or digits");
        }
        return slug;
    }
}
