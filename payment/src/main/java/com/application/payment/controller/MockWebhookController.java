package com.application.payment.controller;

import com.application.payment.dto.PaymentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mockWebhook")
public class MockWebhookController {

    /**
     * MockController to mock external URL for testing
     * @param payment
     * @return
     */
    @PostMapping("/receive")
    public ResponseEntity<String> receiveWebhook(@RequestBody PaymentDTO payment) {
        return ResponseEntity.ok("Webhook received successfully!");
    }
}

