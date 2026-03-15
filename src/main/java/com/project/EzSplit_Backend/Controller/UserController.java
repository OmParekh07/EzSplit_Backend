package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.UserSearchDto;
import com.project.EzSplit_Backend.Service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@AllArgsConstructor
public class UserController {
    private final CustomUserDetailsService userService;
    @GetMapping("/users/search")
    public List<UserSearchDto> searchUsers(
            @RequestParam String email
    ) {
        return userService.searchUsers(email);
    }
}
