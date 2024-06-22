package com.traffic.communication.infraestructure.persistence;

import com.traffic.communication.domain.repository.NotificationRepository;
import com.traffic.communication.domain.entities.Notification;
import com.traffic.communication.domain.entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NotificationRepositoryImpl implements NotificationRepository {



    @Override
    public Optional<Notification> save(Notification notification) {
        return Optional.empty();
    }
}
