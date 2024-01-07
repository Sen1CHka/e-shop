package cz.cvut.fit.tjv.repository;

import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findLessPriceTest() {

        var user1 = new User();
        user1.setUsername("user123");
        user1.setEmail("user123@gmail.com");
        user1.setRealName("User 123");
        user1.setPassword("user123");

        var product1 = new Product();
        product1.setPrice(100D);
        product1.setId(1L);
        product1.setName("product1");
        product1.setDescription("desc1");
        product1.setAvailableAmount(10);
        var product2 = new Product();
        product2.setPrice(200D);
        product2.setId(2L);
        product2.setName("product2");
        product2.setDescription("desc2");
        product2.setAvailableAmount(10);

        userRepository.save(user1);
        productRepository.save(product1);
        productRepository.save(product2);

        var products = productRepository.findLessPrice(150.0);

        assertThat(products).containsOnly(product1);
    }
}
