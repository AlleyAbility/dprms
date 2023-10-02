package com.example.dprms.notification.service;

import com.example.dprms.notification.Notification;
import com.example.dprms.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

//    @Override
//    public List<Notification> getAllNotifications() {
//        return notificationRepository.findAll();
//    }
    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllWithProject();
    }

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }


    @Override
    public boolean delete(Long notificationId) {
        Notification notification = findById(notificationId);
        try {
            notificationRepository.delete(notification);
            return true; // Deletion was successful
        } catch (Exception e) {
            return false; // Deletion was not successful
        }
    }

    public Optional<Notification> findByProjectId(Long projectId) {
        return notificationRepository.findByProjectId(projectId);
    }

    @Override
    public Optional<Notification> findByInstitution(String institutionName) {
        return notificationRepository.findByInstitutionName(institutionName);
    }

    @Override
    public Notification findById(Long projectId) {
        return notificationRepository.findById(projectId).get();
    }


}
