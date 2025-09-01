package com.example.ecommerce.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data  // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appOrderId;        // Your Order ID from your app
    private String razorpayOrderId;   // Razorpay's generated Order ID
    private String razorpayPaymentId; // Razorpay's payment ID
    private String status;            // PENDING, PAID, FAILED
    private Double amount;
    private String currency;

    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;
}
