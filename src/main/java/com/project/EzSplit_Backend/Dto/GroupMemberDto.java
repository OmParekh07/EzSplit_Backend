package com.project.EzSplit_Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GroupMemberDto {

    private Long userId;
    private String name;
    private String email;

}