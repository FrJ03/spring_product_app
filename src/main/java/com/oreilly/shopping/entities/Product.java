package com.oreilly.shopping.entities;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    
    @PositiveOrZero(message = "Price must be zero or greater")
    private BigDecimal price;

    public Product(){}

    public Product(String name, BigDecimal price){
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public BigDecimal getPrice(){
        return price;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPrice(BigDecimal price){
        this.price = price;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;

        var that = (Product) obj;

        return this.id == that.id && 
            this.name == this.name &&
            this.price == this.price;
    }
    @Override
    public int hashCode(){
        return Objects.hash(id, name, price);
    }
    @Override
    public String toString(){
        return "Product{" +
            "id=" + id + "," + 
            "name=" + name + "," + 
            "price=" + price + "}";
    }
}
