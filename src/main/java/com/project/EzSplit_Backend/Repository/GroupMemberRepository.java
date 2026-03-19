package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Group;
import com.project.EzSplit_Backend.Entity.GroupMember;
import com.project.EzSplit_Backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface GroupMemberRepository
        extends JpaRepository<GroupMember, Long> {


    List<GroupMember> findByUser(User user);
    boolean existsByGroupAndUser(Group group, User user);

    List<GroupMember> findByGroup(Group group);

    int countByGroup(Group group);

    void deleteByGroup(Group group);
}