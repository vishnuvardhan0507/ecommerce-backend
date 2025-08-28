package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long id;        // Cart record id
    // User id
    private String username;
    private Long productId; // Product id
    private int quantity;   // Quantity of product
    private String productName;
    private double price;

}
