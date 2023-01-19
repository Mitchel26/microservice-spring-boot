package com.jvaras.product.repository;

import com.jvaras.product.entity.Category;
import com.jvaras.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCategory(Category category);
}
