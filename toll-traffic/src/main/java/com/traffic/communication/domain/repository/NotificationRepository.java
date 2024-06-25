package com.traffic.communication.domain.repository;

import com.traffic.communication.domain.entities.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

   public Optional<Notification> save(Notification notification);

}
