package com.project.EzSplit_Backend.Security;

import com.project.EzSplit_Backend.Entity.AuthUser;
import com.project.EzSplit_Backend.Entity.Type.AuthProviderType;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SupabaseJwtValidator {
    private final UserRepository userRepository;
    @Value("${supabase.project-url}")
    private String projectUrl;

    @Value("${supabase.anon-key}")
    private String anonKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void validate(String token) {

        String url = projectUrl + "/auth/v1/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("apikey", anonKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map body = response.getBody();

        String email = (String) body.get("email");
        String providerId = (String) body.get("id");

        Optional<User> userOpt = userRepository.findByProviderId(providerId);

        User user;

        if(userOpt.isPresent()){

            // OAuth user already linked
            user = userOpt.get();

        }else{

            Optional<User> emailUser = userRepository.findByUsername(email);

            if(emailUser.isPresent()){

                // Existing email/password user → link OAuth account
                user = emailUser.get();

                user.setProviderId(providerId);
                user.setProviderType(AuthProviderType.GOOGLE);

                userRepository.save(user);

            }else{

                // First time OAuth login
                user = new User();
                user.setUsername(email);
                user.setProviderId(providerId);
                user.setProviderType(AuthProviderType.GOOGLE);

            }
        }
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}