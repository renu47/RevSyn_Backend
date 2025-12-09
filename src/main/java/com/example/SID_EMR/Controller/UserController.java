package com.example.SID_EMR.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SID_EMR.DTO.UserDTO;
import com.example.SID_EMR.Entity.User;
import com.example.SID_EMR.Service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
   

    @PostMapping("/create")
    public User registerUser(@RequestBody UserDTO dto) {
        
        return userService.createUser(dto);
    }
}