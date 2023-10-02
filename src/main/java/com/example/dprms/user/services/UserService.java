package com.example.dprms.user.services;

import com.example.dprms.project.repository.ProjectRepository;
import com.example.dprms.exception.UserAlreadyExistsException;
import com.example.dprms.exception.UserNotFoundException;
import com.example.dprms.registration.RegistrationRequest;
import com.example.dprms.registration.token.VerificationToken;
import com.example.dprms.registration.token.VerificationTokenRepository;
import com.example.dprms.role.Role;
import com.example.dprms.role.repository.RoleRepository;
import com.example.dprms.user.User;
import com.example.dprms.user.UserRecord;
import com.example.dprms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<UserRecord> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserRecord(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPosition(),
                        user.getEmployeeId(),
                        user.getPhone(),
                        user.getInstitutionName(),
                        user.getDivision(),
                        new HashSet<>(user.getRoles()),
                        new HashSet<>(user.getProjects()))).collect(Collectors.toList());
    }


    @Override
    public User registerUser(RegistrationRequest request) {
       Optional<User> user = this.findByEmail(request.email());
       if (user.isPresent()){
           throw new UserAlreadyExistsException(
                   "User with email "+request.email() + " already exists");
       }

       Role role = roleRepository.findByName("ROLE_USER").get();
       var newUser = new User();
       newUser.setFirstName(request.firstName());
       newUser.setLastName(request.lastName());
       newUser.setEmail(request.email());
       newUser.setPassword(passwordEncoder.encode(request.password()));
       newUser.setDivision(request.division());
       newUser.setEmployeeId(request.employeeId());
       newUser.setPhone(request.phone());
       newUser.setInstitutionName(request.institutionName());
       newUser.setPosition(request.position());
       newUser.setRoles(Collections.singletonList(role));
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public Optional<User> findByEmployeeId(String employeeId) {
        return userRepository.findByEmployeeId(employeeId);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    @Transactional
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmployeeId(String employeeId) {
        return userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }


    @Override
    public User update(User user) {
        user.setRoles(user.getRoles());
        user.setProjects(user.getProjects());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();

            // Update the fields
            if (updatedUser.getFirstName() != null) {
                userToUpdate.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                userToUpdate.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getEmail() != null) {
                userToUpdate.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPhone() != null) {
                userToUpdate.setPhone(updatedUser.getPhone());
            }
            if (updatedUser.getDivision() != null) {
                userToUpdate.setDivision(updatedUser.getDivision());
            }
            if (updatedUser.getEmployeeId() != null) {
                userToUpdate.setEmployeeId(userToUpdate.getEmployeeId());
            }
            if (updatedUser.getInstitutionName() != null) {
                userToUpdate.setInstitutionName(userToUpdate.getInstitutionName());
            }
            if (updatedUser.getPosition() != null) {
                userToUpdate.setPosition(userToUpdate.getPosition());
            }


            // Save the updated user
            return userRepository.save(userToUpdate);
        } else {
            // Handle the case where the user with the given ID is not found
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }

    @Override
    public User updatePassword(Long id, User updatedPassword) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();

            // Update the fields
            if (updatedPassword.getPassword() != null) {
                userToUpdate.setPassword(passwordEncoder.encode(updatedPassword.getPassword()));
            }
            // Save the updated password
            return userRepository.save(userToUpdate);
        } else {
            // Handle the case where the user with the given ID is not found
            throw new UserNotFoundException("Password not correct");
        }
    }

    @Override
    public List<UserRecord> getUnlockedUsers() {
        return userRepository.findByNotLocked(true)
                .stream()
                .map(user -> new UserRecord(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPosition(),
                        user.getEmployeeId(),
                        user.getPhone(),
                        user.getInstitutionName(),
                        user.getDivision(),
                        new HashSet<>(user.getRoles()),
                        new HashSet<>(user.getProjects()))).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User lockUser(Long id, boolean lock) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNotLocked(lock); // Update the 'notLocked' field based on the 'lock' parameter
            userRepository.save(user);
            return user;
        }
        return null; // Return null if the user with the given ID is not found
    }


    public List<String> getUserRoles(Long userId) {
        return userRepository.getUserRolesByUserId(userId);
    }
}