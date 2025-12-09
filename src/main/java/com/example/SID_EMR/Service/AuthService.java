package com.example.SID_EMR.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SID_EMR.Entity.RefreshToken;
import com.example.SID_EMR.Repository.RefreshTokenRepository;
import com.example.SID_EMR.Security.JwtUtil;

@Service
public class AuthService {
	
	 @Autowired
	    private RefreshTokenRepository refreshTokenRepository;
	    @Autowired
	    private JwtUtil jwtUtil;

	    public String refreshToken(String token) {
	        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
	            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

	        if (refreshToken.isExpired()) {
	            throw new RuntimeException("Refresh token expired");
	        }

	        return jwtUtil.generateToken(refreshToken.getUser().getUsername());
	    }
	

}
