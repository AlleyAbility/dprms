package com.example.dprms.notification.service;

import com.example.dprms.notification.Notification;

import java.util.List;
import java.util.Optional;


public interface INotificationService {
    List<Notification> getAllNotifications();
    Notification createNotification(Notification notification);
    Optional<Notification> findByProjectId(Long projectId);
    Optional<Notification> findByInstitution(String institutionName);
    Notification findById(Long notificationId);
    boolean delete(Long notificationId);
}
