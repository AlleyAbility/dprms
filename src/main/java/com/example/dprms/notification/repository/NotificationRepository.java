package com.example.dprms.notification.repository;

import com.example.dprms.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByProjectId(Long notificationId);
    Optional<Notification> findByInstitutionName(String institutionName);

}
