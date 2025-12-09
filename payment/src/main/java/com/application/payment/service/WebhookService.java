package com.application.payment.service;

import com.application.payment.entity.Webhook;
import com.application.payment.model.WebhookRequest;
import com.application.payment.model.WebhookResponse;
import com.application.payment.repository.WebhookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
public class WebhookService {

    @Autowired
    WebhookRepository webhookRepository;

    public WebhookResponse registerWebhook(WebhookRequest webhookRequest) {
        log.debug("Entered registerWebhook in WebhookService");
        Webhook webhook = new Webhook();
        webhook.setUrl(webhookRequest.getUrl());
        webhook.setClientId(webhookRequest.getClientId());
        webhook.setCreatedAt(OffsetDateTime.now());
        webhook = webhookRepository.save(webhook);
        log.info("Added webhook record to DB: {}", webhook);
        return populateWebhookResponse(webhook);
    }

    private WebhookResponse populateWebhookResponse(Webhook webhook) {
        if(webhook == null){
            return null;
        }
        log.debug("Populating webhook response");
        WebhookResponse webhookResponse = new WebhookResponse();
        webhookResponse.setId(webhook.getId());
        webhookResponse.setUrl(webhook.getUrl());
        webhookResponse.setClientId(webhook.getClientId());
        webhookResponse.setCreatedAt(webhook.getCreatedAt());
        return webhookResponse;
    }

    public List<Webhook> getWebhooksForClients(String clientId){
        log.debug("Entered getWebhookListForClient method in WebhookService {}", clientId);
        List<Webhook> listOfWebhooksForClient = webhookRepository.findByClientId(clientId);
        log.debug("List of webhook for client {}", listOfWebhooksForClient);
        return listOfWebhooksForClient;
    }

}
