package com.example.be.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private String accessToken;
	private String refreshToken;
	private User user;
}