package com.example.SID_EMR.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SID_EMR.DTO.UserDTO;
import com.example.SID_EMR.Entity.User;
import com.example.SID_EMR.Exception.BadRequestException;
import com.example.SID_EMR.Repository.UserRepository;
import com.example.SID_EMR.Util.Logging;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logging log;

    public User createUser(UserDTO dto) {

    	 if (dto.getUsername() == null || dto.getPassword() == null) {
             throw new BadRequestException("Username and password cannot be null");
         }
     	
     	System.out.println("Encoded Password is........"+passwordEncoder.encode(dto.getPassword()));
     	
    	 
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // ✅ ENCODE here
                .roles(dto.getRoles())
                .build();
        log.logAction("User", user.getId(), "CREATE", dto.getUsername(),
                "User created successfully");
        return userRepository.save(user);
    }
}
