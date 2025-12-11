package com.application.payment.service;

import com.application.payment.dto.PaymentDTO;
import com.application.payment.entity.Payment;
import com.application.payment.entity.RetryWebhook;
import com.application.payment.entity.Webhook;
import com.application.payment.model.PaymentRequest;
import com.application.payment.model.PaymentStatus;
import com.application.payment.repository.PaymentRepository;
import com.application.payment.repository.RetryWebhookRepository;
import com.application.payment.repository.WebhookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    EncryptionService encryptionService;

    @Autowired
    WebhookRepository webhookRepository;

    @Autowired
    RetryWebhookRepository retryWebhookRepository;

    @Autowired
    WebClient webClient;

    @Value("${maximum.retry.times}")
    private int maxRetryFrequency;

    @Value("${maximum.retry.duration}")
    private int maxRetryDuration;

    public void createPayment(PaymentRequest paymentRequest) {
        log.debug("Entered createPayment in PaymentService");
        Payment payment = new Payment();
        payment.setFirstName(paymentRequest.getFirstName());
        payment.setLastName(paymentRequest.getLastName());
        payment.setZip(paymentRequest.getZip());
        payment.setClientId(paymentRequest.getClientId());
        payment.setTimestamp(LocalDateTime.now());
        payment.setEncryptedCard(encryptionService.encrypt(paymentRequest.getCardNumber()));
        payment.setAmount(paymentRequest.getAmount());
        payment = paymentRepository.save(payment);
        log.info("Added payment record to DB: {}", payment);
        triggerWebhooks(paymentRequest.getClientId(),payment);

    }

    public void triggerWebhooks(String clientId, Payment payment) {
        List<Webhook> webhooks = webhookRepository.findByClientId(clientId);
        if(webhooks.isEmpty()){
            log.error("Webhook URL is empty for this clientId, please register a webhook");
        }
        for (Webhook webhook : webhooks) {
            log.debug("Webhook record: {}", webhook);
            sendWebhookWithRetry(webhook.getId(), webhook.getUrl(), payment, maxRetryFrequency);
        }
    }


    private void sendWebhookWithRetry(Long webhookId, String url, Payment payment, int maxRetries) {
        log.debug("Entered in sendWebhookWithRetry()");

        RetryWebhook retryRecord = new RetryWebhook(webhookId, payment.getId(), PaymentStatus.PENDING);
        retryWebhookRepository.save(retryRecord);

        PaymentDTO paymentDTO = new PaymentDTO(payment.getId(), webhookId, payment.getClientId(),
                payment.getFirstName(), payment.getLastName(), payment.getZip(), payment.getAmount(),
                payment.getTimestamp(), payment.getEncryptedCard(), PaymentStatus.COMPLETED );

        Mono<Void> request = Mono.defer(() ->
                webClient.post()
                        .uri(url)
                        .bodyValue(paymentDTO)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .doOnSuccess(v -> {
                            log.debug("POST for external webhook is completed");
                            retryRecord.setStatus(PaymentStatus.COMPLETED);
                            retryRecord.setLastAttempt(LocalDateTime.now());
                            retryWebhookRepository.save(retryRecord);
                        })
                        .doOnError(e -> {
                            log.debug("POST for external webhook is failed, retrying after some time...");
                            retryRecord.setRetryCount(retryRecord.getRetryCount() + 1);
                            retryRecord.setLastAttempt(LocalDateTime.now());
                            retryWebhookRepository.save(retryRecord);
                        })
        );

        request.retryWhen(Retry.backoff(maxRetries, Duration.ofSeconds(maxRetryDuration)))
                .onErrorResume(e -> {
                    log.error("Webhook failed after {} retries for paymentId: {}", maxRetries, payment.getId());
                    retryRecord.setStatus(PaymentStatus.FAILED);
                    retryWebhookRepository.save(retryRecord);
                    return Mono.empty();
                })
                .subscribe();
    }

}
