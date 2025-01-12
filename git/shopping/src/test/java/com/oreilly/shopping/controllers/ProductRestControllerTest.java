package com.oreilly.shopping.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import com.oreilly.shopping.entities.Product;

import reactor.core.publisher.Mono;

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

    @Test
    void insertProduct() {
        List<Long> productIds = getIds();
        assertFalse(productIds.contains(999L));
        Product product = new Product("Chair", BigDecimal.valueOf(49.99));
        
        client.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Chair")
                .jsonPath("$.price").isEqualTo(49.99);
    }

    @Test
    void updateProduct() {
        Product product = getProduct(getIds().get(0));
        product.setPrice(product.getPrice().add(BigDecimal.ONE));

        client.put()
                .uri("/products/{id}", product.getId())
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(System.out::println);
    }

    @Test
    void deleteSingleProduct() {
        List<Long> ids = getIds();
        
        assertFalse(ids.isEmpty());

        client.get()
                .uri("/products/{id}", ids.get(0))
                .exchange()
                .expectStatus().isOk();

        client.delete()
                .uri("/products/{id}", ids.get(0))
                .exchange()
                .expectStatus().isNoContent();

        client.get()
                .uri("/products/{id}", ids.get(0))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteAllProducts() {
        List<Long> ids = getIds();

        client.delete()
                .uri("/products")
                .exchange()
                .expectStatus().isNoContent();

        client.get()
                .uri("/products")
                .exchange()
                .expectBodyList(Product.class).hasSize(0);
    }
}
