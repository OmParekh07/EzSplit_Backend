package com.project.EzSplit_Backend.Security;


import com.project.EzSplit_Backend.Dto.LoginRequestDto;
import com.project.EzSplit_Backend.Dto.LoginResponseDto;
import com.project.EzSplit_Backend.Dto.SignUpRequestDto;
import com.project.EzSplit_Backend.Dto.SignupResponseDto;
import com.project.EzSplit_Backend.Entity.Type.AuthProviderType;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final otpService otpService;


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }



    // login controller
    public String signup(SignUpRequestDto signupRequestDto) {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
        if(user != null) throw new IllegalArgumentException("User already exists");
        otpService.sendOtp(signupRequestDto.getUsername());

        return "OTP sent to email";
    }

    public SignupResponseDto verifyOtp(SignUpRequestDto signupRequestDto, String otp){

        boolean verified =
                otpService.verifyOtp(signupRequestDto.getUsername(), otp);

        if(!verified)
            throw new RuntimeException("Invalid OTP");

        User user = userRepository.save(
                User.builder()
                        .username(signupRequestDto.getUsername())
                        .name(signupRequestDto.getName())
                        .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                        .providerType(AuthProviderType.EMAIL)
                        .build()
        );

        return new SignupResponseDto(user.getId(), user.getUsername(),user.getName());


    }

}


















