package com.traffic.communication.Interface;

import com.traffic.communication.domain.entities.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    public void addNotification(Long userId, Notification notification);
    public void addNotification(Notification notification);
    public Optional<List<Notification>> findByUser(Long userId);

}
