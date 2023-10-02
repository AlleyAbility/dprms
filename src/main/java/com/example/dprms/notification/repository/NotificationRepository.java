package com.example.dprms.notification.repository;

import com.example.dprms.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByProjectId(Long notificationId);
    Optional<Notification> findByInstitutionName(String institutionName);

    @Query("SELECT n FROM Notification n JOIN FETCH n.project")
    List<Notification> findAllWithProject();

}
