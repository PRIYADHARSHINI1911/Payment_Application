package com.application.payment.controller;

import com.application.payment.api.PaymentApi;
import com.application.payment.model.PaymentRequest;
import com.application.payment.model.PaymentResponse;
import com.application.payment.service.PaymentService;
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

    @Override
    public ResponseEntity<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
        log.info("Entered create payment in Payment Controller : {}",paymentRequest);
        PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);
        if (paymentResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(paymentResponse);
    }
}
