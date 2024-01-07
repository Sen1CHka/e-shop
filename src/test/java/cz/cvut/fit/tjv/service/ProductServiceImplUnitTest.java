package cz.cvut.fit.tjv.service;

import cz.cvut.fit.tjv.domain.OrderState;
import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.User;
import cz.cvut.fit.tjv.repository.OrderRepository;
import cz.cvut.fit.tjv.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceImplUnitTest {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private ProductRepository productRepository;

    Product product1;
    Product product2;
    Product product3;
    @BeforeEach
    public void setUp() {
        product1 = new Product();
        product1.setPrice(100D);
        product1.setId(1L);
        product1.setName("product1");
        product1.setDescription("desc1");
        product1.setAvailableAmount(10);
        product2 = new Product();
        product2.setPrice(200D);
        product2.setId(2L);
        product2.setName("product2");
        product2.setDescription("desc2");
        product2.setAvailableAmount(10);
        product3 = new Product();
        product2.setPrice(300D);
        product2.setId(3L);
        product2.setName("product3");
        product2.setDescription("desc3");
        product2.setAvailableAmount(10);
    }
    @Test
    void getLessPriceTest() {
        List<Product> mockProducts = List.of(product1,product2,product3);
                when(productRepository.findLessPrice(240D)).thenReturn(mockProducts);

        Collection<Product> products = productService.getLessPrice(240.0);

        assertEquals(mockProducts, products);
        verify(productRepository).findLessPrice(240.0);
    }
}
