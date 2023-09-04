package com.example.dprms.notification;

import com.example.dprms.project.Project;
import com.example.dprms.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // error, approval
    @Column(length = 255)
    private String description;
    private String institutionName;
    private Date createdAt;
    private String status; //redirected, approved, rejected

    @PrePersist
    public void prePersist() {
        createdAt = new Date(); // Set default timestamp value
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
