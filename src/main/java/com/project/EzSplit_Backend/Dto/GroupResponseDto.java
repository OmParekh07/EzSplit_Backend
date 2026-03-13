package com.project.EzSplit_Backend.Dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupResponseDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdAt;
}