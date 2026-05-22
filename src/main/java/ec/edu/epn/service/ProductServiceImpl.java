package ec.edu.epn.service;

import ec.edu.epn.model.Product;
import ec.edu.epn.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found with sku: " + sku));
    }

    @Override
    public List<Product> findActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @Override
    public boolean hasSufficientStock(String sku, int quantity) {
        Product product = findBySku(sku);
        return product.getStock() != null && product.getStock() >= quantity;
    }
}
