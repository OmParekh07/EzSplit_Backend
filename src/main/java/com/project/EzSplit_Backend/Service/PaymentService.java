package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.CreatePaymentRequestDto;
import com.project.EzSplit_Backend.Dto.PaymentResponseDto;
import com.project.EzSplit_Backend.Dto.PaymentViewDto;
import com.project.EzSplit_Backend.Entity.Payment;
import com.project.EzSplit_Backend.Entity.Settlement;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.PaymentRepository;
import com.project.EzSplit_Backend.Repository.SettlementRepository;
import com.project.EzSplit_Backend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.desktop.UserSessionEvent;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {

    @Autowired
    private UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SettlementRepository settlementRepository;
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

    public List<PaymentViewDto> getSettlementPayments(Long settlementId){
        System.out.println("Inside getSettlementPayments, settlementId: " + settlementId);

        List<Payment> payments = paymentRepository.findBySettlement_Id(settlementId);
        System.out.println("Payments size: " + payments.size());
        return payments.stream()
                .map(p -> new PaymentViewDto(
                        p.getId(),
                        p.getPayer().getName(),
                        p.getReceiver().getName(),
                        p.getAmount(),
                        p.getStatus()
                ))
                .toList();
    }
}
