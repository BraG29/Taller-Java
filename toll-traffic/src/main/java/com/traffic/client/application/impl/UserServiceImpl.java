package com.traffic.client.application.impl;

import com.traffic.client.application.UserService;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.repository.ClientModuleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    private ClientModuleRepository clientModuleRepository;

    @Override
    public void registerUser(User user) {

        //TODO try and catch
        clientModuleRepository.createUser(user);
    }
}
