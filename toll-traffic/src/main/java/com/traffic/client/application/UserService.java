package com.traffic.client.application;

import com.traffic.client.domain.User.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public void registerUser(User user);

    public Optional<List<User>> showUsers();
}
