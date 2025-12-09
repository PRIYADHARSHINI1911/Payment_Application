package com.application.payment.util;

import com.application.payment.model.PaymentRequest;
import com.application.payment.model.WebhookRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentUtil {

    public void verifyNullCheck(WebhookRequest webhookRequest) {
        validateObject(webhookRequest, "WebhookRequest");

        validateString(webhookRequest.getUrl(), "URL");
        validateString(webhookRequest.getClientId(), "ClientId");
    }

    public void verifyNullCheck(PaymentRequest paymentRequest) {
        validateObject(paymentRequest, "PaymentRequest");

        validateString(paymentRequest.getFirstName(), "First name");
        validateString(paymentRequest.getLastName(), "Last name");
        validateString(paymentRequest.getZip(), "ZIP");
        validateString(paymentRequest.getCardNumber(), "Card number");
        validateString(paymentRequest.getClientId(), "ClientId");
        validateObject(paymentRequest.getAmount(), "Amount");
    }

    private void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    private void validateObject(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}
