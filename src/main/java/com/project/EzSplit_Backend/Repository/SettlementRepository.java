package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Group;
import com.project.EzSplit_Backend.Entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement,Long> {

    List<Settlement> findByGroup_IdOrderByCreatedAtDesc(Long groupId);

    void deleteByGroup(Group group);

    List<Settlement> findByGroup(Group group);
}
