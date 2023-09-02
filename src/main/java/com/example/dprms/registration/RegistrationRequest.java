package com.example.dprms.registration;

public record RegistrationRequest(
         String firstName,
         String lastName,
         String email,
         String password,
         String position,
         String employeeId,
         String phone,
         String institutionName,
         String division,
         String role,
         String project) {
}
