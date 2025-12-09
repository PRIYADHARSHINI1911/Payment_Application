package com.application.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    private final SecretKey aesSecretKey;

    public String encrypt(String plainText) {
        try {
            byte[] iv = new byte[12];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesSecretKey, new GCMParameterSpec(128, iv));

            byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] ivPlusCipher = new byte[iv.length + cipherBytes.length];
            System.arraycopy(iv, 0, ivPlusCipher, 0, iv.length);
            System.arraycopy(cipherBytes, 0, ivPlusCipher, iv.length, cipherBytes.length);

            return Base64.getEncoder().encodeToString(ivPlusCipher);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedBase64) {
        try {
            byte[] ivPlusCipher = Base64.getDecoder().decode(encryptedBase64);

            byte[] iv = Arrays.copyOfRange(ivPlusCipher, 0, 12);
            byte[] cipherBytes = Arrays.copyOfRange(ivPlusCipher, 12, ivPlusCipher.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesSecretKey, new GCMParameterSpec(128, iv));

            return new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
