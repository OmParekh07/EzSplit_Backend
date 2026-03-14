package com.project.EzSplit_Backend.Security;

import com.project.EzSplit_Backend.Entity.OtpVerification;
import com.project.EzSplit_Backend.Repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class otpService {

    private final OtpRepository otpRepository;
    private final RestTemplate restTemplate;

    @Value("${resend.api.key}")
    private String resendApiKey;

    private final SecureRandom random = new SecureRandom();

    public void sendOtp(String email){
        System.out.println("sendOtp() called for email: " + email);
        String otp = String.valueOf(100000 + random.nextInt(900000));

        OtpVerification record = OtpVerification.builder()
                .email(email)
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .build();

        otpRepository.save(record);

        System.out.println("OTP for " + email + " : " + otp);

        String body = """
        {
          "from": "EzSplit <onboarding@resend.dev>",
          "to": "%s",
          "subject": "EzSplit OTP Verification",
          "html": "<h2>Your OTP is %s</h2><p>Valid for 5 minutes</p>"
        }
        """.formatted(email, otp);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + resendApiKey);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                "https://api.resend.com/emails",
                request,
                String.class
        );
    }

    public boolean verifyOtp(String email, String otp){

        OtpVerification record = otpRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if(record.getExpiryTime().isBefore(LocalDateTime.now()))
            return false;

        if(!record.getOtp().equals(otp))
            return false;

        record.setVerified(true);
        otpRepository.save(record);

        return true;
    }
}