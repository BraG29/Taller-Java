package com.traffic.communication.infraestructure.persistence;

import com.traffic.communication.domain.entities.Notification;
import com.traffic.communication.domain.entities.User;
import com.traffic.communication.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {
    private List<User> users;

    @PostConstruct
    private void initUsers(){
//        users = List.of(
//                new User(1L, "pepe@mail.com", new ArrayList<Notification>()),
//                new User(2L, "miguel@mail.com", new ArrayList<Notification>())
//        );
    }

    @Override
    public Optional<User> save(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> findAll() {
        return Optional.empty();
    }
}
