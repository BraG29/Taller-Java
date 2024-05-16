package com.traffic.client.application.impl;

import com.traffic.client.application.UserService;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.repository.ClientModuleRepository;
import com.traffic.client.domain.repository.ClientModuleRepositoryImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    private ClientModuleRepository clientModuleRepository;

    //inicailizo apra el test.
    public UserServiceImpl(){
        clientModuleRepository = new ClientModuleRepositoryImpl();
    }

    @Override
    public void registerUser(User user) {
        clientModuleRepository.createUser(user);
    }

    @Override
    public Optional<List<User>> showUsers() {
        return  clientModuleRepository.listUsers();
    }
}
