package com.application.payment.dto;

import com.application.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private Long webhookId;
    private String clientId;
    private String firstName;
    private String lastName;
    private String zip;
    private BigDecimal amount;
    private LocalDateTime transactionTime;
    private String encryptedCardNumber;
    private PaymentStatus status;
}
