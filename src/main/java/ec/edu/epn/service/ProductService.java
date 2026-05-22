package ec.edu.epn.service;

import ec.edu.epn.model.Product;
import java.util.List;

public interface ProductService {

    Product findBySku(String sku);

    List<Product> findActiveProducts();

    boolean hasSufficientStock(String sku, int quantity);
}