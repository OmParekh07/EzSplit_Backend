package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.CreatePaymentRequestDto;
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
    public Map<String, Object> createPayment(
            @RequestBody CreatePaymentRequestDto request
    ) {

        Payment payment = new Payment();
        payment.setPayerId(request.getPayerId());
        payment.setReceiverId(request.getReceiverId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        String upiLink = paymentService.generateUpiLink(request.getReceiverUpi(), request.getReceiverName(), request.getAmount());

        Map<String, Object> response = new HashMap<>();
        response.put("paymentId", payment.getId());
        response.put("upiLink", upiLink);

        return response;
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
