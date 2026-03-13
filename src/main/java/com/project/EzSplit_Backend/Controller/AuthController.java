package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.LoginRequestDto;
import com.project.EzSplit_Backend.Dto.LoginResponseDto;
import com.project.EzSplit_Backend.Dto.SignUpRequestDto;
import com.project.EzSplit_Backend.Dto.SignupResponseDto;
import com.project.EzSplit_Backend.Security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignUpRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }




}
