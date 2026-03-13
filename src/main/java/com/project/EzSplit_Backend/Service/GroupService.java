package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.CreateGroupRequestDto;
import com.project.EzSplit_Backend.Dto.GroupResponseDto;
import com.project.EzSplit_Backend.Entity.Group;
import com.project.EzSplit_Backend.Entity.GroupMember;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.GroupMemberRepository;
import com.project.EzSplit_Backend.Repository.GroupRepository;
import com.project.EzSplit_Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public String createGroup(CreateGroupRequestDto request, Long creatorId){

        Group group = groupRepository.save(
                Group.builder()
                        .name(request.getGroupName())
                        .description(request.getDescription())
                        .createdBy(creatorId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // add creator also as member
        groupMemberRepository.save(
                GroupMember.builder()
                        .groupId(group.getId())
                        .userId(creatorId)
                        .build()
        );

        for(String email : request.getMemberEmails()){

            User user = userRepository.findByUsername(email)
                    .orElseThrow(() -> new RuntimeException("User not found: " + email));

            groupMemberRepository.save(
                    GroupMember.builder()
                            .groupId(group.getId())
                            .userId(user.getId())
                            .build()
            );
        }

        return "Group created successfully";
    }

    public List<GroupResponseDto> getGroups(Long userId){

        List<GroupMember> memberships =
                groupMemberRepository.findByUserId(userId);

        List<Long> groupIds = memberships.stream()
                .map(GroupMember::getGroupId)
                .toList();

        List<Group> groups = groupRepository.findByIdIn(groupIds);

        return groups.stream()
                .map(group -> GroupResponseDto.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .description(group.getDescription())
                        .createdAt(group.getCreatedAt())
                        .build())
                .toList();
    }
}