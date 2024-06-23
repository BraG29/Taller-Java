package com.traffic.communication.domain.repository;

import com.traffic.communication.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    public Optional<User> save(User user);
    public Optional<User> findById(Long userId);
    public List<User> findAll();
}
