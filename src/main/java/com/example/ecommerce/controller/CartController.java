package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Add to cart
    @PostMapping("/add")
    public CartDto addToCart(@RequestBody CartDto dto) {
        return cartService.addToCart(dto);
    }

    // Get cart items for a user
    @GetMapping("/user/{username}")
    public List<CartDto> getCartByUsername(@PathVariable String username) {
        return cartService.getCartByUsername(username);
    }
    // Remove item from cart
    @DeleteMapping("/remove/{cartId}")
    public String removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return "Item removed from cart";
    }
    // Update cart item quantity
    @PutMapping("/update/{cartId}")
    public CartDto updateCartQuantity(@PathVariable Long cartId, @RequestParam int delta) {
        return cartService.updateCartQuantity(cartId, delta);
    }

}
