package com.example.dprms.notification;

import com.example.dprms.notification.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private ModelMapper modelMapper;

    private final INotificationService notificationService;

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification){
        return new ResponseEntity<>(notificationService.createNotification(notification), OK);
    }

    @GetMapping("/{id}")
    public Notification getById(@PathVariable("id") Long projectId){
        return  notificationService.findById(projectId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable("id") Long notificationId) {
        boolean deletionSuccessful = notificationService.delete(notificationId);

        if (deletionSuccessful) {
            return ResponseEntity.ok("Notification deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete the notification.");
        }
    }
}
