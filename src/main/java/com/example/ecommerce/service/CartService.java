package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Add product to cart
    public CartDto addToCart(CartDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserAndProduct(user, product);

        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + dto.getQuantity());
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(dto.getQuantity());
        }

        cartRepository.save(cart);

        return toDto(cart);
    }

    // Get cart items for a user
    public List<CartDto> getCartByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Remove item from cart
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // Update cart item quantity
    public CartDto updateCartQuantity(Long cartId, int delta) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQuantity(cart.getQuantity() + delta);

        if (cart.getQuantity() <= 0) {
            cartRepository.delete(cart);
            return null;
        }

        cartRepository.save(cart);
        return toDto(cart);
    }

    // Helper: convert entity → dto
    private CartDto toDto(Cart cart) {
        Product product = cart.getProduct();
        return new CartDto(
                cart.getId(),
                cart.getUser().getUsername(),
                product.getId(),
                cart.getQuantity(),
                product.getName(),
                product.getPrice()
        );
    }
}

