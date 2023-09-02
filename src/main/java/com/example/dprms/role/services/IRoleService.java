package com.example.dprms.role.services;

import com.example.dprms.role.Role;
import com.example.dprms.user.User;

import java.util.List;


public interface IRoleService {
    List<Role> getAllRoles();
    Role createRole(Role theRole);
    void deleteRole(Long roleId);
    Role findByName(String name);
    Role findById(Long roleId);
    User removeUserFromRole(Long userId, Long roleId);
    User assignUserToRole(Long userId, Long roleId);
    Role removeAllUserFromRole(Long roleId);
}
