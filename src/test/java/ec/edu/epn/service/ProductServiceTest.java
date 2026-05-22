package ec.edu.epn.service;

import ec.edu.epn.model.Product;
import ec.edu.epn.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void findBySku_whenProductExists_returnsProduct() {
        Product p = new Product();
        p.setSku("SKU1");
        when(productRepository.findBySku("SKU1")).thenReturn(Optional.of(p));

        Product result = productService.findBySku("SKU1");

        assertSame(p, result);
    }

    @Test
    void findByNegativeSku_whenProductExists_returnsProduct() {
        Product p = new Product();
        p.setSku("SKU1");
        when(productRepository.findBySku("-SKU1")).thenReturn(Optional.of(p));

        Product result = productService.findBySku("-SKU1");

        assertSame(p, result);
    }

    @Test
    void findBySku_whenNotFound_throwsRuntimeException() {
        when(productRepository.findBySku("MISSING")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.findBySku("MISSING"));
    }

    @Test
    void findActiveProducts_returnsActiveList() {
        Product p1 = new Product(); p1.setSku("A"); p1.setActive(true);
        Product p2 = new Product(); p2.setSku("B"); p2.setActive(true);
        when(productRepository.findByActiveTrue()).thenReturn(List.of(p1, p2));

        List<Product> result = productService.findActiveProducts();

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
    }

    @Test
    void hasSufficientStock_trueAndFalse() {
        Product p = new Product(); p.setSku("SKU1"); p.setStock(10);
        when(productRepository.findBySku("SKU1")).thenReturn(Optional.of(p));

        assertTrue(productService.hasSufficientStock("SKU1", 5));
        assertFalse(productService.hasSufficientStock("SKU1", 15));
    }
}
