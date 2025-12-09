package com.application.payment.repository;

import com.application.payment.entity.RetryWebhook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetryWebhookRepository extends JpaRepository<RetryWebhook, Integer> {
}
