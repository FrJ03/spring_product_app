package com.oreilly.shopping.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oreilly.shopping.dao.ProductRepository;
import com.oreilly.shopping.entities.Product;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public void initializeDatabase(){
        if(productRepository.count() == 0){
            productRepository.saveAll(
                List.of(
                    new Product("Tv tray", BigDecimal.valueOf(4.95)),
                    new Product("Toaster", BigDecimal.valueOf(19.95)),
                    new Product("Sofa", BigDecimal.valueOf(249.95))
                )
            ).forEach(System.out::println);
        }
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(Long id){
        return productRepository.findById(id);
    }
}
