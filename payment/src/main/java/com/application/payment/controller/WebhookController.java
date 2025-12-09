package com.application.payment.controller;

import com.application.payment.api.WebhookApi;
import com.application.payment.entity.Webhook;
import com.application.payment.model.WebhookRequest;
import com.application.payment.model.WebhookResponse;
import com.application.payment.service.WebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Slf4j
public class WebhookController implements WebhookApi {

    @Autowired
    WebhookService webhookService;

    @Override
    public ResponseEntity<WebhookResponse> registerWebhook(WebhookRequest webhookRequest) {
        log.info("Entered register webhook in Webhook Controller : {}",webhookRequest);
        WebhookResponse webhookresponse = webhookService.registerWebhook(webhookRequest);
        if (webhookresponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(webhookresponse);
    }
}
