package com.pnam.services.impl;

import com.pnam.pojo.Notification;
import com.pnam.repositories.NotificationRepository;
import com.pnam.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notiRepo;

    @Override
    public Notification getNotificationById(Long id) {
        return notiRepo.findById(id);
    }

    @Override
    public List<Notification> getNotificationsByUser(Long userId) {
        return notiRepo.findByUser(userId);
    }

    @Override
    public Notification createNotification(Notification n) {
        return notiRepo.save(n);
    }

    @Override
    public Notification updateNotification(Notification n) {
        return notiRepo.save(n);
    }

    @Override
    public void deleteNotification(Long id) {
        notiRepo.delete(id);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notiRepo.findAll();
    }

}
