package com.application.payment.controller;

import com.application.payment.api.WebhookApi;
import com.application.payment.model.WebhookRequest;
import com.application.payment.model.WebhookResponse;
import org.springframework.http.ResponseEntity;

public class WebhookController implements WebhookApi {
    @Override
    public ResponseEntity<WebhookResponse> registerWebhook(WebhookRequest webhookRequest) {
        return null;
    }
}
