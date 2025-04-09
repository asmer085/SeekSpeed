package com.example.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.example.payment.controller.PaymentController;
import com.example.payment.controller.EventController;
import com.example.payment.repository.StripeRepo;
import com.example.payment.repository.PaymentRepo;
import com.example.payment.repository.UserRepo;
import com.example.payment.repository.StripeRepo;
import com.example.payment.entity.StripeTransaction;
import com.example.payment.entity.Payment;
@Service
public class StripeService {

    @Autowired
    private StripeRepo stripeTransactionRepository;

    public StripeTransaction createStripeTransaction(Payment payment, String stripeSessionId, String stripePaymentId) {
        StripeTransaction transaction = new StripeTransaction();
        transaction.setPayment(payment);
        transaction.setStripeSessionId(stripeSessionId);
        transaction.setStripePaymentId(stripePaymentId);
        transaction.setStatus("pending");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        return stripeTransactionRepository.save(transaction);
    }

    public void updateTransactionStatus(String stripePaymentId, String status) {
        StripeTransaction transaction = stripeTransactionRepository.findByStripePaymentId(stripePaymentId);
        if (transaction != null) {
            transaction.setStatus(status);
            transaction.setUpdatedAt(LocalDateTime.now());
            stripeTransactionRepository.save(transaction);
        }
    }
}
