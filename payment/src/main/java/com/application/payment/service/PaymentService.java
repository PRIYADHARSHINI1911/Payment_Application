package com.application.payment.service;

import com.application.payment.entity.Payment;
import com.application.payment.model.PaymentRequest;
import com.application.payment.model.PaymentResponse;
import com.application.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    EncryptionService encryptionService;
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        log.debug("Entered createPayment in PaymentService");
        Payment payment = new Payment();
        payment.setFirstName(paymentRequest.getFirstName());
        payment.setLastName(paymentRequest.getLastName());
        payment.setZip(paymentRequest.getZip());
        payment.setClientId(paymentRequest.getClientId());
        payment.setTimestamp(OffsetDateTime.now());
        payment.setEncryptedCard(encryptionService.encrypt(paymentRequest.getCardNumber()));
        payment = paymentRepository.save(payment);
        log.info("Added payment record to DB: {}", payment);
        return populatePaymentResponse(payment);

    }

    private PaymentResponse populatePaymentResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        log.debug("Populating payment response");
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setFirstName(payment.getFirstName());
        response.setLastName(payment.getLastName());
        response.setZip(payment.getZip());
        response.setClientId(payment.getClientId());
        response.setTimestamp(payment.getTimestamp());

        return response;
    }
}
