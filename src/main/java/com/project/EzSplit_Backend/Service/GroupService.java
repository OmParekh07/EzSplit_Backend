package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.CreateGroupRequestDto;
import com.project.EzSplit_Backend.Dto.CreateGroupResponseDto;
import com.project.EzSplit_Backend.Dto.EntryResponseDto;
import com.project.EzSplit_Backend.Dto.GroupResponseDto;
import com.project.EzSplit_Backend.Entity.Group;
import com.project.EzSplit_Backend.Entity.GroupMember;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.*;
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


    public List<GroupResponseDto> getGroups(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GroupMember> memberships =
                groupMemberRepository.findByUser(user);

        return memberships.stream()
                .map(GroupMember::getGroup)
                .map(group -> {

                    List<String> members = groupMemberRepository
                            .findByGroup(group)
                            .stream()
                            .map(member -> member.getUser().getId().toString())
                            .toList();
                    System.out.println("Groups found: " + memberships.size());
//                    List<EntryResponseDto> entries = expenseRepository
//                            .findByGroup(group)
//                            .stream()
//                            .map(expense -> {
//
//                                Map<Long, Double> splits = expenseSplitRepository
//                                        .findByExpense(expense)
//                                        .stream()
//                                        .collect(Collectors.toMap(
//                                                s -> s.getUser().getId(),
//                                                s -> s.getAmount()
//                                        ));
//
//                                return EntryResponseDto.builder()
//                                        .id(expense.getId())
//                                        .description(expense.getDescription())
//                                        .amount(expense.getAmount())
//                                        .paidBy(expense.getPaidBy().getId())
//                                        .splitType(expense.getSplitType())
//                                        .splits(splits)
//                                        .mode(expense.getMode())
//                                        .date(expense.getDate().toString())
//                                        .build();
//                            })
//                            .toList();
                    return GroupResponseDto.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .description(group.getDescription())
                            .createdAt(group.getCreatedAt())
                            .members(members)
                            .entries(List.of())
                            .build();
                })
                .toList();
    }

    public String joinGroup(String inviteCode, Long userId){

        Group group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("Invalid invite code"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(groupMemberRepository.existsByGroupAndUser(group, user))
            return "Already a member";

        groupMemberRepository.save(
                GroupMember.builder()
                        .group(group)
                        .user(user)
                        .build()
        );

        return "Joined group successfully";
    }
}