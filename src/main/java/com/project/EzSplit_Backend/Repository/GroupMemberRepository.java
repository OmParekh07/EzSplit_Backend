package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository
        extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByUserId(Long userId);
}