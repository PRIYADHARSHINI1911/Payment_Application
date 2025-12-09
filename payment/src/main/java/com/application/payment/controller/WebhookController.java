package com.application.payment.controller;

import com.application.payment.api.WebhookApi;
import com.application.payment.model.WebhookRequest;
import com.application.payment.service.WebhookService;
import com.application.payment.util.PaymentUtil;
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

    @Autowired
    PaymentUtil paymentUtil;

    @Override
    public ResponseEntity<String> registerWebhook(WebhookRequest webhookRequest) {
        log.info("Entered register webhook in Webhook Controller : {}",webhookRequest);
        try {
            paymentUtil.verifyNullCheck(webhookRequest);
            webhookService.registerWebhook(webhookRequest);
        }  catch (IllegalArgumentException e) {
            log.error("Invalid input while registering Webhook: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while registering Webhook: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while registering the Webhook.");
        }
        return ResponseEntity.ok("Webhook registered successfully!");
    }
}
