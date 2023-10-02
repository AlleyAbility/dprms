package com.example.dprms.user;

import com.example.dprms.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserRecord>> getUsers(){
        return new ResponseEntity<>(userService.getUnlockedUsers(), HttpStatus.OK);
    }

    @GetMapping("users/{email}")
    public User getByEmail(@PathVariable("email") String email){
        return  userService.getUser(email);
    }

    @GetMapping("users/id/{id}")
    public User getById(@PathVariable("id") Long id){
        return  userService.getUserById(id);
    }

    @GetMapping("users/employee/{employeeId}")
    public User getByEmployeeId(@PathVariable("employeeId") String employeeId){
        return  userService.getUserByEmployeeId(employeeId);
    }

    @DeleteMapping("users/{email}")
    public ResponseEntity<String> delete(@PathVariable("email") String email) {
        try {
            // Attempt to delete the user
            userService.delete(email);

            // If the deletion was successful, return a 204 No Content status
            return ResponseEntity.noContent().build(); // Use noContent() for 204 No Content
        } catch (Exception e) {
            // If an error occurs during deletion, return an error response
            String errorMessage = "Failed to delete user with email: " + email;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


//    @GetMapping("users/id/{email}")
//    public ResponseEntity<Long> getUserIdByEmail(@PathVariable("email") String email) {
//        Long userId = userService.getUserIdByEmail(email);
//
//        if (userId != null) {
//            return ResponseEntity.ok(userId);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }



    @PutMapping("users/{id}/{lock}")
    public ResponseEntity<User> lockUser(
            @PathVariable("id") Long id,
            @PathVariable("lock") boolean lock
    ) {
        User user = userService.lockUser(id, lock);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


//    @PutMapping("/update")
//    public ResponseEntity<User> update(@RequestBody User user){
//        return ResponseEntity.ok(userService.update(user));
//    }

    @PutMapping("users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @RequestBody User updatedUser) {
        User updated = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("users/updatePassword/{id}")
    public ResponseEntity<User> updatePassword(
            @PathVariable("id") Long id,
            @RequestBody User updatedPassword) {
        User updated = userService.updatePassword(id, updatedPassword);
        return ResponseEntity.ok(updated);
    }

}
