package com.application.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class EncryptionConfig {

    @Value("${encryption.aes.key}")
    private String base64Key;

    private static final int TAG_LENGTH = 128;

    @Bean
    public SecretKey aesSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Bean
    public Cipher aesEncryptCipher() throws Exception {
        return Cipher.getInstance("AES/GCM/NoPadding");
    }

    @Bean
    public GCMParameterSpec gcmParameterSpec(byte[] iv) {
        return new GCMParameterSpec(TAG_LENGTH, iv);
    }
}

