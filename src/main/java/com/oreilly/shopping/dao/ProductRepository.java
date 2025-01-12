package com.oreilly.shopping.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oreilly.shopping.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByPriceGreaterThanEqual(BigDecimal minPrice);
}
