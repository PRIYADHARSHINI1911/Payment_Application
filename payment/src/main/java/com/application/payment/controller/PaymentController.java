package com.application.payment.controller;

import com.application.payment.api.PaymentApi;
import com.application.payment.model.PaymentRequest;
import com.application.payment.model.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController implements PaymentApi {


    @Override
    public ResponseEntity<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
        return null;
    }
}
