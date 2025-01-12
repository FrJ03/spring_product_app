package com.oreilly.shopping.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.oreilly.shopping.entities.Product;

@SpringBootTest
@Transactional
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void countProducts(){
        assertEquals(3, productRepository.count());
    }

    @Test
    void findById(){
        assertTrue(productRepository.findById(1L).isPresent());
    }

    @Test
    void findAll(){
        assertEquals(3, productRepository.count());
    }

    @Test
    void insertProduct(){
        Product newProduct = new Product("cricket bat", BigDecimal.valueOf(35.00));
        
        productRepository.save(newProduct);

        assertAll(
            () -> assertNotNull(newProduct.getId()),
            () -> assertEquals(4, productRepository.count())
        );
    }

    @Test
    void deleteProduct(){
        productRepository.deleteById(1L);

        assertEquals(2, productRepository.count());
    }

    @Test
    void deleteAllInBatch(){
        productRepository.deleteAllInBatch();

        assertEquals(0, productRepository.count());
    }

    @Test
    void deleteAllProduct(){
        productRepository.deleteAll();

        assertEquals(0, productRepository.count());
    }
}
