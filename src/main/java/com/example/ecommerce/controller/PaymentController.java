package com.example.ecommerce.controller;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.service.PaymentService;
import com.example.ecommerce.dto.PaymentResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${razorpay.secret}")
    private String secret;
    public PaymentController(PaymentService paymentService,PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public PaymentResponse createOrder(
            @RequestParam String appOrderId,
            @RequestParam Double amount
    ) throws Exception {
        Payment payment = paymentService.createOrder(appOrderId, amount);
        return new PaymentResponse(
                payment.getRazorpayOrderId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus()
        );
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> payload) {
        try {
            String orderId = payload.get("razorpayOrderId");
            String paymentId = payload.get("razorpayPaymentId");
            String signature = payload.get("razorpaySignature");
            // ✅ Prepare JSON for verification
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);

            // ✅ Verify signature with Razorpay Utils
            Utils.verifyPaymentSignature(attributes, secret);

            Payment payment = paymentService.updatePaymentStatus(orderId, paymentId, "PAID");

            return ResponseEntity.ok("Payment verified successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed: " + e.getMessage());
        }
    }


}
