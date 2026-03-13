package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByIdIn(List<Long> groupIds);
}