package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.PaymentResponseDto;
import com.project.EzSplit_Backend.Dto.PaymentViewDto;
import com.project.EzSplit_Backend.Entity.Payment;
import com.project.EzSplit_Backend.Entity.Settlement;
import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.PaymentRepository;
import com.project.EzSplit_Backend.Repository.SettlementRepository;
import com.project.EzSplit_Backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentService {

    @Autowired
    private UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SettlementRepository settlementRepository;
    private final NotificationService notificationService;
    public String generateUpiLink(Payment payment) {

        User receiver = payment.getReceiver();

        String upiId = receiver.getUpiID();

        String name = URLEncoder.encode(receiver.getName(), StandardCharsets.UTF_8);

        return "upi://pay?pa=" + upiId +
                "&pn=" + name +
                "&am=" + payment.getAmount() +
                "&cu=INR" +
                "&tn=EzSplitPayment";
    }

    public List<PaymentViewDto> getSettlementPayments(Long settlementId,Long currentUserId) {
        System.out.println("Inside getSettlementPayments, settlementId: " + settlementId);

        List<Payment> payments = paymentRepository.findBySettlement_Id(settlementId);
        System.out.println("Payments size: " + payments.size());
        return payments.stream()
                .map(p -> {

                    boolean canPay =
                            p.getPayer().getId().equals(currentUserId)
                                    && p.getStatus() == PaymentStatus.PENDING;

                    boolean canConfirm =
                            p.getReceiver().getId().equals(currentUserId)
                                    && p.getStatus() == PaymentStatus.PAID;

                    return PaymentViewDto.builder()
                            .id(p.getId())
                            .payerName(p.getPayer().getName())
                            .receiverName(p.getReceiver().getName())
                            .payerId(p.getPayer().getId())
                            .receiverId(p.getReceiver().getId())
                            .amount(p.getAmount())
                            .status(p.getStatus())
                            .upiLink(generateUpiLink(p))
                            .canPay(canPay)
                            .canConfirm(canConfirm)
                            .build();
                })
                .toList();
    }
    @Transactional
    public void markPaymentPaid(Long paymentId, Long currentUserId) {
        System.out.println("Inside markPaymentPaid, paymentId: " + paymentId + ", currentUserId: " + currentUserId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getPayer().getId().equals(currentUserId)) {
            throw new RuntimeException("Only payer can mark payment as paid");
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        System.out.println("Marking payment as PAID, paymentId: " + paymentId);
        paymentRepository.save(payment);
        paymentRepository.flush();
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        notificationService.createNotification(
                payment.getReceiver(),
                payment.getPayer().getName() + " marked payment as paid ₹" + payment.getAmount()
        );

    }

    @Transactional
    public void confirmPayment(Long paymentId, Long currentUserId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getReceiver().getId().equals(currentUserId)) {
            throw new RuntimeException("Only receiver can confirm");
        }

        payment.setStatus(PaymentStatus.CONFIRMED);

        paymentRepository.save(payment);
        notificationService.createNotification(
                payment.getPayer(),
                payment.getReceiver().getName() + " confirmed your payment ₹" + payment.getAmount()
        );
        checkSettlementCompletion(payment.getSettlement());
    }

    private void checkSettlementCompletion(Settlement settlement) {

        List<Payment> payments = paymentRepository.findBySettlement_Id(settlement.getId());

        boolean allConfirmed = payments.stream()
                .allMatch(p -> p.getStatus() == PaymentStatus.CONFIRMED);

        if (allConfirmed) {
            settlement.setStatus(PaymentStatus.CONFIRMED);
            settlementRepository.save(settlement);
        }
    }
}
