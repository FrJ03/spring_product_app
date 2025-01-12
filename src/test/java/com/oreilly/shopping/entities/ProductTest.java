package com.oreilly.shopping.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Validator;

@SpringBootTest
class ProductTest {
    @Autowired
    private Validator validator;
    
    @Test
    void validProduct(){
        Product product = new Product("TV tray", BigDecimal.valueOf(10.0));

        var validations = validator.validate(product);

        assertTrue(validations.isEmpty());
    }
    @Test
    void emptyNameProduct(){
        Product product = new Product(" ", BigDecimal.valueOf(10.0));

        var validations = validator.validate(product);

        assertFalse(validations.isEmpty());
        assertEquals(1, validations.size());
    }
    @Test
    void negativePriceProduct(){
        Product product = new Product("TV tray", BigDecimal.valueOf(-10.0));

        var validations = validator.validate(product);

        assertFalse(validations.isEmpty());
        assertEquals(1, validations.size());
    }
    @Test
    void emptyNameAndNegativePriceProduct(){
        Product product = new Product(" ", BigDecimal.valueOf(-10.0));

        var validations = validator.validate(product);

        assertFalse(validations.isEmpty());
        assertEquals(2, validations.size());
    }
}
