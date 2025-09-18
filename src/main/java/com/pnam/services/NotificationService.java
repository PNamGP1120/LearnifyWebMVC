package com.pnam.services;

import com.pnam.pojo.Notification;
import java.util.List;

public interface NotificationService {

    Notification getNotificationById(Long id);

    List<Notification> getNotificationsByUser(Long userId);

    Notification createNotification(Notification n);

    Notification updateNotification(Notification n);

    void deleteNotification(Long id);

    List<Notification> getAllNotifications();
}
