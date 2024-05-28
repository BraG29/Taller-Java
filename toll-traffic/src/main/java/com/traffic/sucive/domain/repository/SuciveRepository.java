package com.traffic.sucive.domain.repository;

import com.traffic.sucive.domain.user.User;

import java.util.List;

public interface SuciveRepository {

    public void addUser(User user);

    public List<User> getAllUsers();
}
