package com.example.dprms.Project;

import com.example.dprms.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projectName;
    private String projectManager;
    private String projectVendor;
    private String projectSponsor;
    private String institutionName;
    private Date createdAt;
    private String status;

    @PrePersist
    public void prePersist() {
        createdAt = new Date(); // Set default timestamp value
    }

//    public String getFormattedCreatedAt() {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return formatter.format(createdAt);
//    }


    @JsonIgnore
    @ManyToMany(mappedBy = "projects") // Update 'mappedBy' to match the property name in User
    private Collection<User> users = new HashSet<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "institution_id")
//    private Institution institution;


    public Project(String projectName) {
        this.projectName = projectName;
    }
    public void removeAllUsersFromProject(){
        if (this.getUsers() != null){
            List<User> usersInProject = this.getUsers().stream().toList();
            usersInProject.forEach(this::removeUserFromProject);
        }
    }
    public void removeUserFromProject(User user) {
        user.getProjects().remove(this);
        this.getUsers().remove(user);
    }
    public void assignUserToRole(User user){
        user.getProjects().add(this);
        this.getUsers().add(user);
    }

}
