package com.project.EzSplit_Backend.Dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateGroupRequestDto {

    private String groupName;

    private String description;

    private List<String> memberEmails;
}