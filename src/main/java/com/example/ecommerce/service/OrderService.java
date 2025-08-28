package com.example.ecommerce.service;
import java.util.*;
import com.example.ecommerce.dto.*;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @Transactional
    public OrderDto placeOrder(OrderDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ fetch cart items
        List<CartDto> cartItems = cartService.getCartByUsername(user.getUsername());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartDto cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProductId()));

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // Reduce stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Create order item
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(cartItem.getQuantity());
            oi.setPrice(product.getPrice());

            orderItems.add(oi);

            // ✅ update total
            totalAmount += product.getPrice() * cartItem.getQuantity();

        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        // ✅ Save order with items
        Order savedOrder = orderRepository.save(order);

        // ✅ Clear cart
        cartRepository.deleteByUser(user);

        // Build response DTO
        OrderDto response = new OrderDto();
        response.setId(savedOrder.getId());
        response.setUsername(user.getUsername());
        response.setOrderDate(savedOrder.getOrderDate());
        response.setTotalAmount(savedOrder.getTotalAmount());

        List<OrderItemDto> itemDtos = savedOrder.getItems().stream().map(oi -> {
            OrderItemDto oid = new OrderItemDto();
            oid.setProductId(oi.getProduct().getId());
            oid.setName(oi.getProduct().getName());
          //  oid.setDescription(oi.getProduct().getDescription());
            oid.setPrice(oi.getPrice());
            oid.setQuantity(oi.getQuantity());
            return oid;
        }).toList();

        response.setItems(itemDtos);

        return response;
    }

    public List<OrderDto> getOrdersByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user).stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setUsername(user.getUsername());
            dto.setOrderDate(order.getOrderDate());
            dto.setTotalAmount(order.getTotalAmount());

            List<OrderItemDto> itemDtos = order.getItems().stream().map(oi -> {
                OrderItemDto oid = new OrderItemDto();
                oid.setProductId(oi.getProduct().getId());
                oid.setName(oi.getProduct().getName());
               // oid.setDescription(oi.getProduct().getDescription());
                oid.setPrice(oi.getPrice());
                oid.setQuantity(oi.getQuantity());
                return oid;
            }).toList();

            dto.setItems(itemDtos);
            return dto;
        }).toList();
    }


    }


