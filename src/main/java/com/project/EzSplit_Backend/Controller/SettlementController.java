package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.PaymentViewDto;
import com.project.EzSplit_Backend.Dto.SettlementTransactionDto;
import com.project.EzSplit_Backend.Entity.Payment;
import com.project.EzSplit_Backend.Entity.Settlement;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.PaymentRepository;
import com.project.EzSplit_Backend.Repository.SettlementRepository;
import com.project.EzSplit_Backend.Service.PaymentService;
import com.project.EzSplit_Backend.Service.SettlementService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class SettlementController {
    private final SettlementService service;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final SettlementRepository settlementRepository;
    @PostMapping("/settle-up/{groupId}")
    public List<SettlementTransactionDto> settleUp(@PathVariable Long groupId) {
        return service.generateSettlements(groupId);
    }
    @GetMapping("/settlement/{settlementId}/payments")
    public List<PaymentViewDto> getPaymentsOfSettlement(@PathVariable("settlementId") Long settlementId, Authentication authentication) {
        System.out.println("Fetching payments for settlement ID: " + settlementId);
        User user = (User) authentication.getPrincipal();
        return paymentService.getSettlementPayments(settlementId,user.getId());
    }

    @GetMapping("/debug/payments")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll(); // check if ANY payments exist
    }

    @GetMapping("/debug/settlements")
    public List<Settlement> getAllSettlements() {
        return settlementRepository.findAll(); // check if settlements exist
    }

}
