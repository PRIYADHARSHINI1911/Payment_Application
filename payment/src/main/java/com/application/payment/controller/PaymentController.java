package com.application.payment.controller;

import com.application.payment.api.PaymentApi;
import com.application.payment.model.PaymentRequest;
import com.application.payment.service.PaymentService;
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
public class PaymentController implements PaymentApi {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentUtil paymentUtil;

    @Override
    public ResponseEntity<String> createPayment(PaymentRequest paymentRequest) {
        log.info("Entered create payment in Payment Controller : {}",paymentRequest);
        try {
            paymentUtil.verifyNullCheck(paymentRequest);
            paymentService.createPayment(paymentRequest);
        } catch (IllegalArgumentException e) {
            log.error("Invalid input while creating payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while creating payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while processing the payment.");
        }

        return ResponseEntity.ok("Payment created successfully!");
    }
}
