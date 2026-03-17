package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.CreatePaymentRequestDto;
import com.project.EzSplit_Backend.Dto.PaymentResponseDto;
import com.project.EzSplit_Backend.Entity.Payment;
import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.PaymentRepository;
import com.project.EzSplit_Backend.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    @PostMapping("/{paymentId}/paid")
    public void markPaid(@PathVariable Long paymentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        paymentService.markPaymentPaid(paymentId, user.getId());
    }

    @PostMapping("/{paymentId}/confirm")
    public void confirmPayment(@PathVariable Long paymentId,Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        paymentService.confirmPayment(paymentId, user.getId());
    }
}
