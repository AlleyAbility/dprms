package com.example.dprms.user;

import com.example.dprms.user.DTO.UserDTO;
import com.example.dprms.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all")
    public ResponseEntity<List<UserRecord>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.FOUND);
    }

    @GetMapping("/{email}")
    public User getByEmail(@PathVariable("email") String email){
        return  userService.getUser(email);
    }

    @DeleteMapping("/{email}")
    public void delete(@PathVariable("email") String email){
        userService.delete(email);
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user){
        return ResponseEntity.ok(userService.update(user));
    }

}
