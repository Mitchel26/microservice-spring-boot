package com.jvaras.product;

import com.jvaras.product.entity.Category;
import com.jvaras.product.entity.Product;
import com.jvaras.product.repository.ProductRepository;
import com.jvaras.product.services.ProductService;
import com.jvaras.product.services.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
        Product computer = Product.builder()
                .id(1L)
                .name("computer")
                .category(Category.builder().id(1L).build())
                .price(Double.parseDouble("12.5"))
                .stock(Double.parseDouble("5"))
                .build();

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(computer));
        Mockito.when(productRepository.save(computer)).thenReturn(computer);
    }

    @Test
    public void whenValidGetId_ThenReturnProduct() {
        Product found = productService.getProduct(1L);
        Assertions.assertEquals(found.getName(), "computer");
    }

    @Test
    public void whenValidUpdateStock_ThenReturnNewStok() {
        Product newStock = productService.updateStock(1L, Double.parseDouble("10"));
        Assertions.assertEquals(newStock.getStock(), 15);
    }
}
