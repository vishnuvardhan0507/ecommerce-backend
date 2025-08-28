package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.mapper.OrderMapper;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    // Place a new order
    @PostMapping("/place")
    public OrderDto placeOrder(@RequestBody OrderDto dto) {
        return orderService.placeOrder(dto);
    }

    // Get orders for a specific user
    @GetMapping("/user/{username}")
    public List<OrderDto> getOrdersByUser(@PathVariable String username) {
        return orderService.getOrdersByUser(username);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allOrders")
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
