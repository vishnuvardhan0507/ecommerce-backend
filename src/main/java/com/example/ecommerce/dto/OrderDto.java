package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String  username;
   // private List<Long> productIds;
    private LocalDateTime orderDate;
    private double totalAmount;

  //  private List<ProductDto> products;
    private List<OrderItemDto> items;
}
