package com.example.dprms.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // You can invalidate the user's session here if needed
        request.getSession().invalidate();

        // Clear the authentication in the SecurityContext to log the user out
        SecurityContextHolder.clearContext();

        // Redirect or respond as needed after logout
        return "Logged out successfully";
    }
}
