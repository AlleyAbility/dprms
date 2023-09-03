package com.example.dprms.user.DTO;

import com.example.dprms.project.Project;
import com.example.dprms.role.Role;
import lombok.Data;

import java.util.Collection;

@Data
public class UserDTO {

    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String position;
    private String employeeId;
    private String institutionName;
    private String division;
    private Collection<Role> roles;
    private Collection<Project> projects;

}
