package com.oreilly.shopping.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.oreilly.shopping.entities.Product;
import com.oreilly.shopping.services.ProductService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/products")
public class ProductRestController {
    @Autowired
    private ProductService service;

    public ProductRestController(ProductService service){
        this.service = service;
    }

    @GetMapping
    public List<Product> getProducts() {
        return service.findAllProducts();
    }

    @GetMapping(params = "min")
    public List<Product> getProducts(@RequestParam(defaultValue = "0.0") double min) {
        if(min < 0)
            throw new ProductMinimumPriceException(min);

        return service.findAllProductsByMinPrice(min);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.of(service.findProductById(id));
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product p = service.saveProduct(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(p.getId())
            .toUri();

        return ResponseEntity.created(location).body(p);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product){
        return service.findProductById(id)
            .map(p -> {
                p.setName(product.getName());
                p.setPrice(product.getPrice());

                return ResponseEntity.ok(service.saveProduct(p));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        return service.findProductById(id)
            .map(p -> {
                service.deleteProduct(p);

                return ResponseEntity.noContent().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllProducts(){
        service.deleteAllProducts();

        return ResponseEntity.noContent().build();
    }
}
