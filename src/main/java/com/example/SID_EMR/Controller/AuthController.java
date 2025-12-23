package com.example.SID_EMR.Controller;



import com.example.SID_EMR.DTO.LoginRequestDTO;
import com.example.SID_EMR.DTO.RefreshTokenRequest;
import com.example.SID_EMR.DTO.TokenResponse;
import com.example.SID_EMR.DTO.UserProfileResponseDTO;
import com.example.SID_EMR.Entity.RefreshToken;
import com.example.SID_EMR.Entity.User;
import com.example.SID_EMR.Repository.RefreshTokenRepository;
import com.example.SID_EMR.Repository.UserRepository;
import com.example.SID_EMR.Security.JwtUtil;
import com.example.SID_EMR.Service.AuthService;
import com.example.SID_EMR.Service.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final  RefreshTokenService refreshTokenService;

    private final RefreshTokenRepository refreshTokenRepository;

 
   
    @GetMapping("/welcome")
    public String Welcome() {
        return "Welcome test......";
    }
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getCurrentUser(
            Authentication authentication
    ) {
        // Authentication comes from Spring Security context
        String username = authentication.getName(); // <-- from JWT subject

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileResponseDTO response = new UserProfileResponseDTO(
                user.getUsername(),
                user.getRoles()
        );

        return ResponseEntity.ok(response);
    }
   

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new TokenResponse(newAccessToken, request.getRefreshToken(), // send the same refresh token back
                "Bearer"));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDTO dto) {
        return userRepository.findByUsername(dto.getUsername())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> {
                    // 1️⃣ Generate Access Token
                    String accessToken = jwtUtil.generateToken(user.getUsername());

                    // 2️⃣ Create Refresh Token using service
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

                    // 3️⃣ Build response with both tokens
                    TokenResponse tokenResponse = new TokenResponse(
                            accessToken,
                            refreshToken.getToken(),
                            "Bearer"
                    );

                    return ResponseEntity.ok(tokenResponse);
                })
                .orElse(ResponseEntity.status(401).build());
    }
    
    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<Void> logout(Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user);

        return ResponseEntity.ok().build();
    }
    
   
}