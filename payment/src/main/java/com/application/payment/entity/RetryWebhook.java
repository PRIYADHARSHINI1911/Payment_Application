package com.application.payment.entity;

import com.application.payment.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "retry_webhook")
public class RetryWebhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long webhookId;

    private Long paymentId;

    private Integer retryCount = 0;

    private LocalDateTime lastAttempt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public RetryWebhook(Long webhookId, Long paymentId, PaymentStatus status){
        this.webhookId = webhookId;
        this.paymentId = paymentId;
        this.status = status;
        this.retryCount = 0;
        this.lastAttempt = LocalDateTime.now();
    }
}

