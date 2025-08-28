package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.dto.OrderItemDto;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto toDTO(Order order) {
        List<OrderItemDto> itemDTOs = order.getItems().stream()
                .map(OrderMapper::mapOrderItemToDTO)
                .collect(Collectors.toList());

        return new OrderDto(
                order.getId(),
                order.getUser().getUsername(),
                order.getOrderDate(),
                order.getTotalAmount(),
                itemDTOs
        );
    }

    private static OrderItemDto mapOrderItemToDTO(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getProduct().getName(),
                item.getPrice(),
                item.getQuantity()
        );
    }
}
