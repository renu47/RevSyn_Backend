package com.example.SID_EMR.Repository;


import com.example.SID_EMR.Entity.RefreshToken;
import com.example.SID_EMR.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends  JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);
}
