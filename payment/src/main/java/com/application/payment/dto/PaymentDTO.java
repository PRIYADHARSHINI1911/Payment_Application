package com.application.payment.dto;

import com.application.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private Long webhookId;
    private String clientId;
    private String encryptedCardNumber;
    private PaymentStatus status;
}
