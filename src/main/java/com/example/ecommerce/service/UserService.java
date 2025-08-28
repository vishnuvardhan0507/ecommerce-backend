package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
