package com.example.dprms.registration;

import com.example.dprms.event.RegistrationCompleteEvent;
import com.example.dprms.registration.token.VerificationToken;
import com.example.dprms.registration.token.VerificationTokenRepository;
import com.example.dprms.user.User;
import com.example.dprms.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    private Environment environment;

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl()));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Success! Please check your email to complete your registration");

        return ResponseEntity.ok(response);
    }


//    @GetMapping("/users/verifyEmail")
//    public String verifyEmail(@RequestParam("token") String token){
//        VerificationToken theToken = tokenRepository.findByToken(token);
//        if (theToken.getUser().isEnabled()){
//            return "This account has already been verified, please, login.";
//        }
//        String verificationResult = userService.validateToken(token);
//        if (verificationResult.equalsIgnoreCase("valid")){
//            return "Email verified successfully. Now you can login to your account";
//        }
//        return "Invalid verification token";
//    }

    @GetMapping("/users/verifyEmail")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("token") String token) {
        // Find the verification token in the repository
        VerificationToken theToken = tokenRepository.findByToken(token);

        if (theToken == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid verification token");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if the associated user's email is already verified
        if (theToken.getUser().isEnabled()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "This account has already been verified, please, login.");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate the token and mark the user's email as verified
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            theToken.getUser().setEnabled(true); // Mark the user's email as verified
            // userService.updateUser(theToken.getUser()); // Update the user in the database

            Map<String, String> response = new HashMap<>();
            response.put("message", "Email verified successfully. Now you can login to your account");
            return ResponseEntity.ok(response);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid verification token");
        return ResponseEntity.badRequest().body(response);
    }



    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
//        return "http://"+request.getServerName()+":"+4200+request.getContextPath();
    }

    public String applicationUrl() {
        // Use the frontend URL from application properties
        return environment.getProperty("frontend.url");
    }
}
