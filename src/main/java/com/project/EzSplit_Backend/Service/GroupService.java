package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.*;
import com.project.EzSplit_Backend.Entity.Group;
import com.project.EzSplit_Backend.Entity.GroupMember;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final SettlementRepository settlementRepository;
    private final PaymentRepository paymentRepository;

    public CreateGroupResponseDto createGroup(CreateGroupRequestDto request, Long creatorId){

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));
        String inviteCode = UUID.randomUUID().toString().substring(0,8);
        Group group = groupRepository.save(
                Group.builder()
                        .name(request.getGroupName())
                        .description(request.getDescription())
                        .createdBy(creator)
                        .inviteCode(inviteCode)
                        .build()
        );

        // add creator also as member
        groupMemberRepository.save(
                GroupMember.builder()
                        .group(group)
                        .user(creator)
                        .build()
        );

        for(String email : request.getMemberEmails()){

            User user = userRepository.findByUsername(email)
                    .orElseThrow(() -> new RuntimeException("User not found: " + email));

            groupMemberRepository.save(
                    GroupMember.builder()
                            .group(group)
                            .user(user)
                            .build()
            );
        }

        return CreateGroupResponseDto.builder()
                .groupId(group.getId())
                .inviteCode(inviteCode)
                .inviteLink("/api/v1/groups/join/" + inviteCode)
                .build();
    }




    public List<GroupSummaryDto> getGroups(Long userId){

        List<Group> groups = groupRepository.findGroupsByUserId(userId);

        return groups.stream().map(group -> {

            int membersCount = groupMemberRepository.countByGroup(group);

            int totalExpenses = expenseRepository.countByGroup(group);

            Double totalSpent = expenseRepository.sumAmountByGroup(group);

            if(totalSpent == null) totalSpent = 0.0;

            return new GroupSummaryDto(
                    group.getId(),
                    group.getName(),
                    group.getDescription(),
                    membersCount,
                    totalExpenses,
                    totalSpent
            );

        }).toList();
    }

    public JoinGroupResponseDto joinGroup(String inviteCode, Long userId){

        Group group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("Invalid invite code"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(groupMemberRepository.existsByGroupAndUser(group, user)){
            return new JoinGroupResponseDto(
                    group.getId(),
                    group.getName(),
                    false,
                    "Already a member"
            );
        }

        groupMemberRepository.save(
                GroupMember.builder()
                        .group(group)
                        .user(user)
                        .build()
        );

        return new JoinGroupResponseDto(
                group.getId(),
                group.getName(),
                true,
                "Successfully joined group"
        );
    }

    public String getInviteCode(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Invalid group id"));
        return group.getInviteCode();
    }

    public GroupDetailDto getGroupDetails(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupMember> members =
                groupMemberRepository.findByGroup(group);

        List<GroupMemberDto> memberDtos =
                members.stream()
                        .map(gm -> new GroupMemberDto(
                                gm.getUser().getId(),
                                gm.getUser().getName(),
                                gm.getUser().getUsername()
                        ))
                        .toList();

        return new GroupDetailDto(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getInviteCode(),
                group.getCreatedBy().getName(),
                memberDtos.size(),
                memberDtos
        );
    }

    @Transactional
    public String deleteGroup(Long groupId, Long id) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if(!group.getCreatedBy().getId().equals(id)){
            throw new RuntimeException("Only group creator can delete the group");
        }

        // delete expense splits
        List<Long> expenseIds = expenseRepository.findByGroup(group)
                .stream()
                .map(e -> e.getId())
                .toList();


        //delete payments


        List<Long> settlementIds = settlementRepository.findByGroup(group)
                .stream()
                .map(s -> s.getId())
                .toList();
        paymentRepository.deleteBySettlementIdIn(settlementIds);
        //delete settlements
        settlementRepository.deleteByGroup(group);

        //remove splits
        expenseSplitRepository.deleteByExpenseIdIn(expenseIds);
        // delete expenses
        expenseRepository.deleteByGroup(group);

        // delete group members
        groupMemberRepository.deleteByGroup(group);

        // delete group
        groupRepository.delete(group);



        return "Group deleted successfully";
    }
}