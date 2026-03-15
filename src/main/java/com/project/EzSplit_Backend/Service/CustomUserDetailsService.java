package com.project.EzSplit_Backend.Service;
import com.project.EzSplit_Backend.Dto.UserSearchDto;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public List<UserSearchDto> searchUsers(String email) {

        List<User> users = userRepository
                .findByUsernameContainingIgnoreCase(email);

        return users.stream()
                .map(u -> new UserSearchDto(
                        u.getId(),
                        u.getName(),
                        u.getUsername()
                ))
                .toList();
    }
}

