package com.jvaras.product.services;

import com.jvaras.product.entity.Category;
import com.jvaras.product.entity.Product;
import com.jvaras.product.repository.ProductRepository;
import com.jvaras.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> listAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        product.setCreateAt(new Date());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product productdb = getProduct(product.getId());
        if (productdb == null) {
            return null;
        }
        productdb.setName(product.getName());
        productdb.setDescription(product.getDescription());
        productdb.setCategory(product.getCategory());
        productdb.setPrice(product.getPrice());

        return productRepository.save(productdb);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product productdb = getProduct(id);
        if (productdb == null) {
            return null;
        }

        productdb.setStatus("DELETED");
        return productRepository.save(productdb);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return  productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product productdb = getProduct(id);
        if (productdb == null){
            return null;
        }
        Double sotck = productdb.getStock() + quantity;
        productdb.setStock(sotck);
        return  productRepository.save(productdb);
    }
}
