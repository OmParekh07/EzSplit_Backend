package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.CreateUserRequest;
import com.project.EzSplit_Backend.Dto.UserSearchDto;

import com.project.EzSplit_Backend.Entity.Type.AuthProviderType;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.UserRepository;
import com.project.EzSplit_Backend.Service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@AllArgsConstructor
public class UserController {
    private final CustomUserDetailsService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/users/search")
    public List<UserSearchDto> searchUsers(
            @RequestParam String email
    ) {
        return userService.searchUsers(email);
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(Authentication authentication,
                                        @RequestBody CreateUserRequest request) {


        User authUser = (User)authentication.getPrincipal();

        String email = authUser.getUsername();
        String providerId = authUser.getProviderId();
        System.out.println(email + "  "+providerId);
        System.out.println(request);
        User user = new User();

        user.setUsername(email);
        user.setProviderId(providerId);
        user.setProviderType(AuthProviderType.GOOGLE);
        user.setName(request.getName());
        user.setUpiID(request.getUpiId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User created");
    }
}
