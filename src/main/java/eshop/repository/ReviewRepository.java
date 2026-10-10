package eshop.repository;

import eshop.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    interface RatingStats {
        Long getProductId();

        Double getAverage();

        Long getCount();
    }

    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    Optional<Review> findByProductIdAndUserUsername(Long productId, String username);

    void deleteByUserId(Long userId);

    @Query("""
            select r.product.id as productId, avg(r.rating) as average, count(r) as count
            from Review r where r.product.id in :ids group by r.product.id
            """)
    List<RatingStats> ratingStats(@Param("ids") Collection<Long> ids);
}
