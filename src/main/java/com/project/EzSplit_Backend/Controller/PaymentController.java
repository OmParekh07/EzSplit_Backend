package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.CreatePaymentRequestDto;
import com.project.EzSplit_Backend.Dto.PaymentResponseDto;
import com.project.EzSplit_Backend.Entity.Payment;
import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import com.project.EzSplit_Backend.Repository.PaymentRepository;
import com.project.EzSplit_Backend.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create")
    public PaymentResponseDto createPayment(
            @RequestBody CreatePaymentRequestDto request
    ) {
        return paymentService.generateUpiLink(request);
    }

    @PostMapping("/confirm/{id}")
    public String confirmPayment(@PathVariable Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);

        return "Payment confirmed successfully";
    }

    @PostMapping("/cancel/{id}")
    public String cancelPayment(@PathVariable Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.CANCELLED);

        paymentRepository.save(payment);

        return "Payment cancelled";
    }

    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
