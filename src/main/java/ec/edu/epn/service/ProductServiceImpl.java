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
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Product> findActiveProducts() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean hasSufficientStock(String sku, int quantity) {
        throw new RuntimeException("Not implemented");
    }
}
