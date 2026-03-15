package com.project.EzSplit_Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearchDto {

    private Long id;
    private String name;
    private String email;

}