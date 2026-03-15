package com.project.EzSplit_Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JoinGroupResponseDto {

    private Long groupId;
    private String groupName;
    private boolean joined;
    private String message;

}