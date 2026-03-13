package com.project.EzSplit_Backend.Entity;


import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long payerId;
    private Long receiverId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING','PAID','CANCELLED')")
    private PaymentStatus status;

    private LocalDateTime createdAt;

}

