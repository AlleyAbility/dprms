package com.example.dprms.user;


import com.example.dprms.Project.Project;
import com.example.dprms.role.Role;

import java.util.HashSet;
import java.util.Set;

public record UserRecord(
        Long id,
        String firstName,
        String lastName,
        String email,
        String position,
        String employeeId,
        String phone,
        String institutionName,
        String division,
        Set<Role> roles,
        Set<Project> projects) {}
