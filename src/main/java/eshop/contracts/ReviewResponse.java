package eshop.contracts;

import eshop.domain.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long productId,
        String username,
        String realName,
        int rating,
        String comment,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(review.getId(), review.getProduct().getId(), review.getUser().getUsername(),
                review.getUser().getRealName(), review.getRating(), review.getComment(), review.getCreatedAt());
    }
}
