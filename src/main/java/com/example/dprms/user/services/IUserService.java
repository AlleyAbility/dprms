package com.example.dprms.user.services;

import com.example.dprms.registration.RegistrationRequest;
import com.example.dprms.user.User;
import com.example.dprms.user.UserRecord;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    List<UserRecord> getUsers();

    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    Optional<User> findByEmployeeId(String employeeId);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    void delete(String email);
    User getUser(String email);

    User getUserById(Long id);

    User getUserByEmployeeId(String employeeId);
    User update(User user);
    User updateUser(Long id, User updatedUser);

    User updatePassword(Long id, User updatedPassword);

    List<UserRecord> getUnlockedUsers();
    User lockUser(Long id, boolean lock);
}
