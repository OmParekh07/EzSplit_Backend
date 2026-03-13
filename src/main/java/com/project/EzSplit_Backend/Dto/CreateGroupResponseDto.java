package com.project.EzSplit_Backend.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class CreateGroupResponseDto {

    private Long groupId;

    private String inviteCode;

    private String inviteLink;
}