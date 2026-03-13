package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.CreateGroupRequestDto;
import com.project.EzSplit_Backend.Dto.GroupResponseDto;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public String createGroup(@RequestBody CreateGroupRequestDto request,
                              Authentication authentication){

        User user = (User) authentication.getPrincipal();

        return groupService.createGroup(request, user.getId());
    }

    @GetMapping
    public List<GroupResponseDto> getGroups(Authentication authentication){

        User user = (User) authentication.getPrincipal();

        return groupService.getGroups(user.getId());
    }

}