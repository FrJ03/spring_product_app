package com.oreilly.shopping.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.oreilly.shopping.services.ProductService;

@Component
public class AppInit implements CommandLineRunner {
    private final ProductService productService;

    public AppInit(ProductService productService){
        this.productService = productService;
    }

    @Override
    public void run(String... args){
        productService.initializeDatabase();
    }
    
}
