package com.oreilly.shopping.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.oreilly.shopping.entities.Product;
import com.oreilly.shopping.services.ProductService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class ProductRestController {
    @Autowired
    private ProductService service;

    public ProductRestController(ProductService service){
        this.service = service;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return service.findAllProducts();
    }
}
