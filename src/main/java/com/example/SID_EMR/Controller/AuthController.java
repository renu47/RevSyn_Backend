package com.example.SID_EMR.Controller;



import com.example.SID_EMR.DTO.LoginRequestDTO;
import com.example.SID_EMR.DTO.RefreshTokenRequest;
import com.example.SID_EMR.DTO.TokenResponse;
import com.example.SID_EMR.Entity.RefreshToken;
import com.example.SID_EMR.Repository.UserRepository;
import com.example.SID_EMR.Security.JwtUtil;
import com.example.SID_EMR.Service.AuthService;
import com.example.SID_EMR.Service.RefreshTokenService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

   
    @GetMapping("/welcome")
    public String Welcome() {
        return "Welcome......";
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
    
   
}