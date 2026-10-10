package eshop.service;

import eshop.contracts.ReviewRequest;
import eshop.contracts.ReviewResponse;
import eshop.domain.Product;
import eshop.domain.Review;
import eshop.domain.User;
import eshop.exception.ApiException;
import eshop.repository.ProductRepository;
import eshop.repository.ReviewRepository;
import eshop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewResponse> getByProduct(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    /**
     * Every user has one review per product; sending another one rewrites it.
     */
    @Transactional
    public ReviewResponse save(Long productId, String username, ReviewRequest request) {
        String comment = request.comment() == null || request.comment().isBlank() ? null : request.comment().trim();
        Review review = reviewRepository.findByProductIdAndUserUsername(productId, username).orElse(null);
        if (review == null) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> ApiException.notFound("Product not found"));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> ApiException.notFound("User not found"));
            review = new Review(product, user, request.rating(), comment);
        } else {
            review.setRating(request.rating());
            review.setComment(comment);
            review.setCreatedAt(LocalDateTime.now());
        }
        return ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional
    public void delete(Long id, String username, boolean admin) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Review not found"));
        if (!admin && !review.getUser().getUsername().equals(username)) {
            throw ApiException.forbidden("You can delete only your own review");
        }
        reviewRepository.delete(review);
    }
}
