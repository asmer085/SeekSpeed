package com.example.payment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import com.example.payment.repository.StripeRepo;
import com.example.payment.repository.PaymentRepo;
import com.example.payment.entity.Payment;
import com.example.payment.entity.StripeTransaction;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final StripeRepo stripeTransactionRepository;
    private final PaymentRepo paymentRepository;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                ObjectMapper objectMapper = new ObjectMapper();
                // Pretvori Stripe objekt u JSON string i potom parsiraj sa Jackson
                JsonNode data = objectMapper.readTree(event.getData().getObject().toJson());

                String sessionId = data.get("id").asText();
                String paymentStatus = data.get("payment_status").asText();

                Payment payment = paymentRepository.findByStripePaymentId(sessionId).orElse(null);
                if (payment != null) {
                    payment.setPaymentStatus(paymentStatus);
                    paymentRepository.save(payment);

                    StripeTransaction transaction = new StripeTransaction();
                    transaction.setPayment(payment);
                    transaction.setStripeSessionId(sessionId);
                    transaction.setStatus(paymentStatus);
                    transaction.setAmountReceived(payment.getAmount());
                    transaction.setPaymentMethod("card"); // Može se proširiti
                    stripeTransactionRepository.save(transaction);
                }
            }
            return ResponseEntity.ok("Webhook processed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Webhook error: " + e.getMessage());
        }
    }
}
