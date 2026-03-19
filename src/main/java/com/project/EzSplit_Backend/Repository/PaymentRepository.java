package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Payment;
import com.project.EzSplit_Backend.Entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findBySettlement_Id(Long settlementId);

    @Query("""
    SELECT SUM(p.amount)
    FROM Payment p
    WHERE p.payer.id = :userId
    """)
    Double sumYouOwe(Long userId);


    @Query("""
    SELECT SUM(p.amount)
    FROM Payment p
    WHERE p.receiver.id = :userId
    """)
    Double sumOwedToYou(Long userId);

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.settlement.id IN :ids")
    void deleteBySettlementIdIn(List<Long> ids);
}
