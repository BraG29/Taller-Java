package com.traffic.communication.Interface.impl;

import com.traffic.communication.Interface.NotificationRepository;
import com.traffic.communication.domain.entities.Notification;
import com.traffic.communication.domain.entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NotificationRepositoryImpl implements NotificationRepository {

    private List<User> users;

    @PostConstruct
    private void initUsers(){
        users = List.of(
                new User(1L, "pepe@mail.com", new ArrayList<Notification>()),
                new User(2L, "miguel@mail.com", new ArrayList<Notification>())
        );
    }

    @Override
    public void addNotification(Long userId, Notification notification) {

        Optional<User> userOPT = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();

        userOPT.ifPresent( u -> {
            u.getNotifications().add(notification);
        });

    }

    @Override
    public void addNotification(Notification notification) {
        users.forEach(u -> {
            u.getNotifications().add(notification);
        });
    }

    @Override
    public Optional<List<Notification>> findByUser(Long userId) {

        Optional<List<Notification>> out = Optional.empty();

        Optional<User> userOPT = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();

        if(userOPT.isPresent()){
            out = Optional.of(userOPT.get().getNotifications());
        }

        return out;
    }
}
