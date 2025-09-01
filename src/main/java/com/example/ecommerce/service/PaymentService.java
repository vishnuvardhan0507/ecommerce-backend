package com.example.ecommerce.service;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;
    private final String razorpayKey;

    public PaymentService(
            PaymentRepository paymentRepository,
            @Value("${razorpay.key}") String key,
            @Value("${razorpay.secret}") String secret
    ) throws Exception {
        this.paymentRepository = paymentRepository;
        this.razorpayKey = key;
        this.razorpayClient = new RazorpayClient(key, secret);
    }

    public Payment createOrder(String appOrderId, Double amount) throws Exception {
        // Razorpay accepts amount in paise
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_" + appOrderId);

        Order order = razorpayClient.orders.create(orderRequest);

        // Save in DB
        Payment payment = new Payment();
        payment.setAppOrderId(appOrderId);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setStatus("PENDING");
        paymentRepository.save(payment);
         return payment;
    }
    public Payment updatePaymentStatus(String orderId, String paymentId, String status) {
        Payment payment = paymentRepository.findByRazorpayOrderId(orderId);

        payment.setRazorpayPaymentId(paymentId);
        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }


}
