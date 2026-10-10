package eshop.contracts;

import eshop.domain.Category;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        String icon,
        long productCount
) {
    public static CategoryResponse from(Category category, long productCount) {
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug(),
                category.getDescription(), category.getIcon(), productCount);
    }
}
