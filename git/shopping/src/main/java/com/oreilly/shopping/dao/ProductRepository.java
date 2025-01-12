package com.oreilly.shopping.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oreilly.shopping.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
