package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement,Long> {

    List<Settlement> findByGroup_IdOrderByCreatedAtDesc(Long groupId);
}
