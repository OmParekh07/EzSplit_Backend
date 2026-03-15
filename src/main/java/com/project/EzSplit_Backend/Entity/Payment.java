package com.project.EzSplit_Backend.Entity;

import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // who paid
    @ManyToOne
    @JoinColumn(name = "payer_id")
    private User payer;

    // who received
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    @ManyToOne
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;
}