package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Entity.AuthUser;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class SupaAuthController {
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        User authUser = (User)authentication.getPrincipal();

        String email = authUser.getUsername();
        String providerId = authUser.getProviderId();

        Optional<User> userOpt = userRepository.findByUsername(email);

        Map<String, Object> response = new HashMap<>();

        if(userOpt.isPresent()){

            User user = userOpt.get();

            response.put("status", "EXISTING_USER");
            response.put("email", user.getUsername());
            response.put("id",user.getId());

        }else{

            response.put("status", "NEW_USER");
            response.put("email", email);

        }

        return ResponseEntity.ok(response);
    }
}
