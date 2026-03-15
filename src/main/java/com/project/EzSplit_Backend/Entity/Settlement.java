package com.project.EzSplit_Backend.Entity;

import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import jakarta.persistence.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "for_group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private LocalDateTime createdAt;

}