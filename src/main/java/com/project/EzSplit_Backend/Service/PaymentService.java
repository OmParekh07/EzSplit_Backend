package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.CreatePaymentRequestDto;
import com.project.EzSplit_Backend.Dto.PaymentResponseDto;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.desktop.UserSessionEvent;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    public PaymentResponseDto generateUpiLink(CreatePaymentRequestDto createPaymentRequestDto) {
        User receiver=userRepository.findById(createPaymentRequestDto.getReceiverId())
                .orElseThrow(()->new RuntimeException("User not found"));

        String upiId=receiver.getUpiID();

        String upiLink="upi://pay?pa=" + upiId +
                "&pn=" + receiver.getName() +
                "&am=" + createPaymentRequestDto.getAmount() +
                "&cu=INR";

        return new PaymentResponseDto(
                createPaymentRequestDto.getPayerId(),
                createPaymentRequestDto.getReceiverId(),
                createPaymentRequestDto.getAmount(),
                upiLink
        );

    }
}
