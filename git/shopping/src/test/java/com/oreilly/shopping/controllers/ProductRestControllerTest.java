package com.oreilly.shopping.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import com.oreilly.shopping.entities.Product;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ProductRestControllerTest {
    @Autowired
    private WebTestClient client;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Long> getIds(){
        return jdbcTemplate.queryForList("SELECT id FROM product", Long.class);
    }

    private Product getProduct(Long id){
        return jdbcTemplate.queryForObject(
            "SELECT * FROM product WHERE id = ?",
            (resultSet, rowCount) -> new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("price")
            ),
            id
        );
    }
    
    @Test
    void getAllProducts(){
        List<Long> productsIds = getIds();

        assertFalse(productsIds.contains(999L));

        client.get()
            .uri("/products")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Product.class).hasSize(3);
    }

    @ParameterizedTest(name = "Product ID: {0}")
    @MethodSource("getIds")
    void getProductThatExists(Long id){
        client.get()
            .uri("/products/{id}", id)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(id);
    }

    @Test
    void getProductThatDoesNotExists(){
        List<Long> productsIds = getIds();

        assertFalse(productsIds.contains(999L));

        client.get()
            .uri("/products/999")
            .exchange()
            .expectStatus().isNotFound();
    }
}
