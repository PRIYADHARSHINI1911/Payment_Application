package com.application.payment.controller;

import com.application.payment.api.WebhookControllerApi;
import com.application.payment.model.StringResponse;
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
@RequestMapping("")
@Slf4j
public class WebhookController implements WebhookControllerApi {

    @Autowired
    WebhookService webhookService;

    @Autowired
    PaymentUtil paymentUtil;

    @Override
    public ResponseEntity<StringResponse> registerWebhook(WebhookRequest webhookRequest) {
        log.info("Entered register webhook in Webhook Controller : {}",webhookRequest);
        StringResponse stringResponse = new StringResponse();
        try {
            paymentUtil.verifyNullCheck(webhookRequest);
            webhookService.registerWebhook(webhookRequest);
        }  catch (IllegalArgumentException e) {
            log.error("Invalid input while registering Webhook: {}", e.getMessage());
            stringResponse.setMessage("Invalid request data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(stringResponse);
        } catch (Exception e) {
            log.error("Unexpected error while registering Webhook: {}", e.getMessage());
            stringResponse.setMessage("An unexpected error occurred while registering the Webhook.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(stringResponse);
        }
        stringResponse.setMessage("Webhook registered successfully!");
        return ResponseEntity.ok(stringResponse);
    }
}
