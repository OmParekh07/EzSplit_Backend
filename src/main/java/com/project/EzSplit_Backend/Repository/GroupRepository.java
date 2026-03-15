package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("""
       SELECT gm.group
       FROM GroupMember gm
       WHERE gm.user.id = :userId
       """)
    List<Group> findGroupsByUserId(Long userId);
    List<Group> findByIdIn(List<Long> groupIds);
    Optional<Group> findByInviteCode(String inviteCode);
}