package com.example.payment.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private Double amount;
    private String currency;
    private String paymentStatus;
    private String stripePaymentId;
    private Long userId;
    private Long eventId;
}
