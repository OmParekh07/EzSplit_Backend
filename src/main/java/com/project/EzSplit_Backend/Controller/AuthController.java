package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.LoginRequestDto;
import com.project.EzSplit_Backend.Dto.LoginResponseDto;
import com.project.EzSplit_Backend.Dto.SignUpRequestDto;
import com.project.EzSplit_Backend.Dto.SignupResponseDto;
import com.project.EzSplit_Backend.Security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JavaMailSender mailSender;
    @Autowired
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    @PostMapping("/verify-otp")
    public SignupResponseDto verifyOtp(@RequestBody SignUpRequestDto request,
                                       @RequestParam String otp){
        return authService.verifyOtp(request, otp);
    }

    @GetMapping("/test-mail")
    public String testMail(){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("connect.omparekh@gmail.com");
        message.setSubject("Test Email");
        message.setText("Mail working");

        mailSender.send(message);

        return "Mail sent";
    }




}
