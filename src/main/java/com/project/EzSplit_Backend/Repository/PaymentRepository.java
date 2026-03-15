package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Payment;
import com.project.EzSplit_Backend.Entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findBySettlement_Id(Long settlementId);
}
