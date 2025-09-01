// src/main/java/com/ecommerce/repository/PaymentRepository.java
package com.example.ecommerce.repository;

import com.example.ecommerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByRazorpayOrderId(String razorpayOrderId);
}
