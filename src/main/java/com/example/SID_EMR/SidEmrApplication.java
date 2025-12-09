package com.example.SID_EMR;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SidEmrApplication implements CommandLineRunner{

	@Value("${spring.security.user.name}")
	public String username;
	public static void main(String[] args) {
		SpringApplication.run(SidEmrApplication.class, args);
		
		
	}
	
	@Override
    public void run(String... args) {
        System.out.println("App Title: " + username);
    }

}
