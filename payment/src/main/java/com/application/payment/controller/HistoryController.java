package com.application.payment.controller;

import com.application.payment.dto.PaymentDTO;
import com.application.payment.entity.Payment;
import com.application.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
@Slf4j
public class HistoryController {

    @Autowired
    PaymentRepository paymentRepository;

    /**
     * GET /api/history : Get all payment history
     *
     * @return List of all payments
     */
    @GetMapping("/api/history")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        log.info("Fetching all payments history");
        try {
            List<Payment> payments = paymentRepository.findAll();
            List<PaymentDTO> paymentDTOs = payments.stream().map(payment -> {
                PaymentDTO dto = new PaymentDTO();
                dto.setPaymentId(payment.getId());
                dto.setClientId(payment.getClientId());
                dto.setFirstName(payment.getFirstName());
                dto.setLastName(payment.getLastName());
                dto.setZip(payment.getZip());
                dto.setAmount(payment.getAmount());
                dto.setTransactionTime(payment.getTimestamp());
                dto.setEncryptedCardNumber(payment.getEncryptedCard());
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(paymentDTOs);
        } catch (Exception e) {
            log.error("Error fetching payment history: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
