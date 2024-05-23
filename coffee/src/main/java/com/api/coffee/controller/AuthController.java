package com.api.coffee.controller;

import com.api.coffee.DTO.LoginDTO;
import com.api.coffee.entity.User;
import com.api.coffee.repository.UserRepository;
import com.api.coffee.service.JwtService;
import com.api.coffee.utils.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody LoginDTO loginDto) {
        logger.info("Auth server started correctly for : {}", loginDto.getEmail());
        try {
            Authentication authUser = authenticateUser(loginDto.getEmail(), loginDto.getPassword());
            String jwtToken = jwtService.generateToken(authUser);
            logger.info("Operation finished with success");
            return ResponseEntity.ok(jwtToken);
        } catch (BadCredentialsException e) {
            logger.error("Incorrect email or password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password");
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        logger.info("Started correctly registration for user: {}", user.getEmail());
        if (isInvalidUser(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Incorrect data. Email or password is null.");
        }

        if (userExists(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists");
        }

        try {
            registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            logger.error("An error occurred during registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration. Please try again.");
        }
    }

    private Authentication authenticateUser(String email, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private boolean isInvalidUser(User user) {
        return user == null || user.getEmail() == null || user.getPassword() == null;
    }

    private boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodedPassword);
        user.setAccountType(AccountType.USER);
        userRepository.save(user);
    }
}
