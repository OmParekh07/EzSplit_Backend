package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.CreateGroupRequestDto;
import com.project.EzSplit_Backend.Dto.CreateGroupResponseDto;
import com.project.EzSplit_Backend.Dto.GroupResponseDto;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<CreateGroupResponseDto> createGroup(@RequestBody CreateGroupRequestDto request,
                                                              Authentication authentication){

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(groupService.createGroup(request, user.getId()));
    }

    @GetMapping
    public List<GroupResponseDto> getGroups(Authentication authentication){

        User user = (User) authentication.getPrincipal();

        return groupService.getGroups(user.getId());
    }

    @PostMapping("/join/{inviteCode}")
    public String joinGroup(@PathVariable String inviteCode,
                            Authentication authentication){

        User user = (User) authentication.getPrincipal();

        return groupService.joinGroup(inviteCode, user.getId());
    }

}