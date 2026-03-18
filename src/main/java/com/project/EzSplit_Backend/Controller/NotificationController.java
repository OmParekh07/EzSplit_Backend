package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Entity.Notification;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<Notification> getNotifications(
            Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return notificationRepository
                .findTop10ByUser_IdOrderByCreatedAtDesc(user.getId());
    }
}