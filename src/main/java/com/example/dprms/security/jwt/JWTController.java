package com.example.dprms.security.jwt;


import com.example.dprms.security.userDetails.UserRegistrationDetailsService;
import com.example.dprms.user.User;
import com.example.dprms.user.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
//@RequestMapping("/login")
public class JWTController {
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRegistrationDetailsService userRegistrationDetailsService;

    private PasswordEncoder passwordEncoder;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Value("${spring.jwt.secret}")
//    private String jwtSecret;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @PostMapping("/login")
    public ResponseEntity<String> getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest) {
        // Retrieve the user's stored password from the database based on the provided email
        Optional<User> userDetails = userService.findByEmail(authRequest.getEmail());

        // Encode the provided password using the same encoding algorithm (BCryptPasswordEncoder)
        String encodedProvidedPassword = bCryptPasswordEncoder.encode(authRequest.getPassword());

        // Compare the encoded provided password with the stored password
        if (bCryptPasswordEncoder.matches(authRequest.getPassword(), userDetails.get().getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {

                // Get the user's ID from the retrieved user details
                Long userId = userDetails.get().getId();

                // Get user roles from the database
                List<String> userRoles = userService.getUserRoles(userId);

                // Generate a secure key for HS512 algorithm
                byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
                // Convert the keyBytes to a string or store it securely
                String jwtSecret = Base64.getEncoder().encodeToString(keyBytes);

                // Create claims and include user ID and roles
                Claims claims = Jwts.claims().setSubject(userDetails.get().getEmail());
                claims.put("userId", userId);
                claims.put("roles", userRoles); // Include user roles in claims

                // Generate the JWT token with claims
                String jwt = Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS512, jwtSecret)
                        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                        .compact();

                // Generate JWT token with user information
//                final String jwt = jwtService.generateToken(userId, userDetails.get().getEmail(), userRoles);

                return ResponseEntity.ok(jwt);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}