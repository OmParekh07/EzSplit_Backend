package com.project.EzSplit_Backend.Dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String name;
    private String upiId;
    private String password;

}