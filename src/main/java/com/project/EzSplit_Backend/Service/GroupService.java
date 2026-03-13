package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.CreateGroupRequestDto;
import com.project.EzSplit_Backend.Dto.CreateGroupResponseDto;
import com.project.EzSplit_Backend.Dto.GroupResponseDto;
import com.project.EzSplit_Backend.Entity.Group;
import com.project.EzSplit_Backend.Entity.GroupMember;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.GroupMemberRepository;
import com.project.EzSplit_Backend.Repository.GroupRepository;
import com.project.EzSplit_Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

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
                .map(member -> member.getGroup())
                .map(group -> GroupResponseDto.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .createdAt(group.getCreatedAt())
                        .build())
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