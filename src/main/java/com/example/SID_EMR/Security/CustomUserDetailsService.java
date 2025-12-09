package com.example.SID_EMR.Security;


import com.example.SID_EMR.Entity.User;
import com.example.SID_EMR.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private  UserRepository userRepository;
	
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
     // convert CSV roles -> List<GrantedAuthority>
        List<SimpleGrantedAuthority> authorities = Optional.ofNullable(userEntity.getRoles())
            .map(r -> Arrays.stream(r.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(SimpleGrantedAuthority::new)        // create GrantedAuthority
                    .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
        

        return org.springframework.security.core.userdetails.User
                .withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(authorities)   // pass a Collection<? extends GrantedAuthority>
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

	}
}
