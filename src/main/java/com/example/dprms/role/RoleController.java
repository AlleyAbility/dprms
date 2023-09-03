package com.example.dprms.role;

import com.example.dprms.role.services.IRoleService;
import com.example.dprms.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;


@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getAllRoles(), FOUND);
    }
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        return new ResponseEntity<>(roleService.createRole(role), CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Long roleId){
        roleService.deleteRole(roleId);
    }

    @GetMapping("/{id}")
    public Role getById(@PathVariable("id") Long roleId){
        return  roleService.findById(roleId);
    }
    @PostMapping("/remove-all-users-from-role/{id}")
    public Role removeAllUsersFromRole(@PathVariable("id") Long roleId){
       return roleService.removeAllUserFromRole(roleId);
    }
    @PostMapping("/remove-user-from-role")
    public User removeUserFromRole(@RequestParam("userId")Long userId,
                                   @RequestParam("roleId") Long roleId){
       return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-user-to-role")
    public User assignUserToRole(@RequestParam("userId")Long userId,
                                   @RequestParam("roleId") Long roleId){
        return roleService.assignUserToRole(userId, roleId);
    }
}
