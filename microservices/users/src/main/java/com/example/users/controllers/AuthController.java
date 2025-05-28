package com.example.users.controllers;

import com.example.users.dto.LoginDTO;
import com.example.users.dto.TokenRefreshRequest;
import com.example.users.dto.UserDTO;
import com.example.users.entity.Users;
import com.example.users.mappers.UserMapper;
import com.example.users.repository.UserRepository;
import com.example.users.security.JwtUtil;

import com.example.users.services.JwtBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtBlacklistService blacklistService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users dbUser = userRepository.findByUsername(user.getUsername());

        String accessToken = jwtUtils.generateToken(userDetails.getUsername(), dbUser.getRole());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

        return ResponseEntity.ok().body(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Error: Username is already taken!";
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!user.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            throw new IllegalArgumentException("Password must contain at least one letter and one number");
        }
        user.setPassword(encoder.encode(user.getPassword()));

        Users user2 = userMapper.userDTOToUsers(user);
        userRepository.save(user2);
        return "User created successfully!";
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            long expMillis = jwtUtils.getExpiration(token).getTime() - System.currentTimeMillis();
            blacklistService.blacklistToken(token, expMillis);
        }
        return ResponseEntity.ok("Logged out and token blacklisted.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || !jwtUtils.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        String username = jwtUtils.getUsernameFromToken(refreshToken);
        Users dbUser = userRepository.findByUsername(username);
        if (dbUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String newAccessToken = jwtUtils.generateToken(username, dbUser.getRole());
        return ResponseEntity.ok().body(Map.of(
                "accessToken", newAccessToken
        ));
    }


}

