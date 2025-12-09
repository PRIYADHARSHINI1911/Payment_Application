package com.application.payment.controller;

import com.application.payment.dto.PaymentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dummywebhook")
public class DummyWebhookController {

    @PostMapping("/receive")
    public ResponseEntity<String> receiveWebhook(@RequestBody PaymentDTO payment) {
        return ResponseEntity.ok("Webhook received successfully!");
    }
}

