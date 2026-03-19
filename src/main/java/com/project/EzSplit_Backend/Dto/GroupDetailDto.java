package com.project.EzSplit_Backend.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupDetailDto {

    private Long groupId;
    private String name;
    private String description;
    private String inviteCode;
    private String creatorName;
    private int membersCount;

    private List<GroupMemberDto> members;

}
