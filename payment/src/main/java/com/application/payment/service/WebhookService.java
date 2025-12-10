package com.application.payment.service;

import com.application.payment.entity.Webhook;
import com.application.payment.model.WebhookRequest;
import com.application.payment.repository.WebhookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class WebhookService {

    @Autowired
    WebhookRepository webhookRepository;

    public void registerWebhook(WebhookRequest webhookRequest) {
        log.debug("Entered registerWebhook in WebhookService");
        Webhook webhook = new Webhook();
        webhook.setUrl(webhookRequest.getUrl());
        webhook.setClientId(webhookRequest.getClientId());
        webhook.setCreatedAt(LocalDateTime.now());
        webhook = webhookRepository.save(webhook);
        log.info("Added webhook record to DB: {}", webhook);
    }

}
