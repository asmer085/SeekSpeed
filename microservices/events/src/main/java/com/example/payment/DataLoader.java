package com.example.payment;

import com.example.payment.entity.Event;
import com.example.payment.entity.Payment;
import com.example.payment.entity.StripeTransaction;
import com.example.payment.entity.User;
import com.example.payment.repository.EventRepo;
import com.example.payment.repository.PaymentRepo;
import com.example.payment.repository.StripeRepo;
import com.example.payment.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private StripeRepo stripeTransactionRepo;

    @Override
    public void run(String... args) throws Exception {

        // Dodavanje korisnika
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setName("John Doe");
        userRepo.save(user);

        // Dodavanje događaja
        Event event = new Event();
        event.setName("Spring Boot Workshop");
        event.setPrice(100.00);
        eventRepo.save(event);

        // Dodavanje plaćanja
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setEvent(event);
        payment.setAmount(100.00);
        payment.setCurrency("USD");
        payment.setPaymentStatus("COMPLETED");
        payment.setStripePaymentId("stripe_payment_id_123");
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(payment);

        // Dodavanje Stripe transakcije
        StripeTransaction stripeTransaction = new StripeTransaction();
        stripeTransaction.setPayment(payment);
        stripeTransaction.setStripeChargeId("charge_123");
        stripeTransaction.setStripeSessionId("session_123");
        stripeTransaction.setStripePaymentId("stripe_payment_id_123");
        stripeTransaction.setStatus("SUCCEEDED");
        stripeTransaction.setAmountReceived(100.00);
        stripeTransaction.setPaymentMethod("Credit Card");
        stripeTransaction.setCreatedAt(LocalDateTime.now());
        stripeTransaction.setUpdatedAt(LocalDateTime.now());
        stripeTransactionRepo.save(stripeTransaction);
    }
}
