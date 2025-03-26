package com.example.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.payment.entity.StripeTransaction;
@Repository
public interface StripeRepo extends JpaRepository<StripeTransaction, Long> {
    StripeTransaction findByStripePaymentId(String stripePaymentId);
}
