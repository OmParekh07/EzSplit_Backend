package com.project.EzSplit_Backend.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class otpService {

    private final JavaMailSender mailSender;

    private final Map<String,String> otpStore = new ConcurrentHashMap<>();
    private final Map<String,Long> expiryStore = new ConcurrentHashMap<>();


    public void sendOtp(String email){

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpStore.put(email, otp);
        expiryStore.put(email, System.currentTimeMillis() + 300000);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ez.split.otp@gmail.com");   // IMPORTANT
        message.setTo(email);
        message.setSubject("Welcome \nEzSplit OTP Verification(Valid for 5 minutes)");
        message.setText("Your OTP is: " + otp);

        System.out.println("Sending OTP: " + otp);

        mailSender.send(message);

        System.out.println("OTP email sent");
    }


    public boolean verifyOtp(String email, String otp){

        if(!otpStore.containsKey(email)) return false;

        if(System.currentTimeMillis() > expiryStore.get(email)){
            otpStore.remove(email);
            expiryStore.remove(email);
            return false;
        }

        if(otpStore.get(email).equals(otp)){
            otpStore.remove(email);
            expiryStore.remove(email);
            return true;
        }

        return false;
    }
}