package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findTop10ByUser_IdOrderByCreatedAtDesc(Long userId);

}