package com.pnam.repositories;

import com.pnam.pojo.Notification;
import java.util.List;

public interface NotificationRepository {
    Notification findById(Long id);
    List<Notification> findByUser(Long userId);
    Notification save(Notification n);
    void delete(Long id);
    List<Notification> findAll();

}
