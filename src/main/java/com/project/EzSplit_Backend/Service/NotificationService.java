package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Entity.Notification;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(User user, String message){

        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

}