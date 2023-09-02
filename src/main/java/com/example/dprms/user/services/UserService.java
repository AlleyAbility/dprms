package com.example.dprms.user.services;

import com.example.dprms.Project.Project;
import com.example.dprms.Project.repository.ProjectRepository;
import com.example.dprms.exception.UserAlreadyExistsException;
import com.example.dprms.exception.UserNotFoundException;
import com.example.dprms.registration.RegistrationRequest;
import com.example.dprms.registration.token.VerificationTokenRepository;
import com.example.dprms.role.Role;
import com.example.dprms.role.repository.RoleRepository;
import com.example.dprms.user.User;
import com.example.dprms.registration.token.VerificationToken;
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
                        user.getDivision(),
                        user.getInstitutionName(),
                        user.getPhone(),
                        user.getEmployeeId(),
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
       Project project = projectRepository.findByProjectName("").get();
       var newUser = new User();
       newUser.setFirstName(request.firstName());
       newUser.setLastName(request.lastName());
       newUser.setEmail(request.email());
       newUser.setPassword(passwordEncoder.encode(request.password()));
       newUser.setDivision(request.division());
       newUser.setEmployeeId(request.employeeId());
       newUser.setPhone(request.employeeId());
       newUser.setInstitutionName(request.institutionName());
       newUser.setPosition(request.position());
       newUser.setRoles(Collections.singletonList(role));
       newUser.setProjects(Collections.singletonList(project));
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public User update(User user) {
        user.setRoles(user.getRoles());
        user.setProjects(user.getProjects());
        return userRepository.save(user);
    }

}
