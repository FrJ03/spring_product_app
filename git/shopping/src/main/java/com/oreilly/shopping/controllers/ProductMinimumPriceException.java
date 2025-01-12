package com.oreilly.shopping.controllers;

public class ProductMinimumPriceException extends RuntimeException {
    public ProductMinimumPriceException(){
        this("Minimum price must be greater or equal than zero");
    }
    
    public ProductMinimumPriceException(String message) {
        super(message);
    }

    public ProductMinimumPriceException(double minPrice){
        this("Minimum price must be greater or equal than zero, but was " + minPrice);
    }
}
