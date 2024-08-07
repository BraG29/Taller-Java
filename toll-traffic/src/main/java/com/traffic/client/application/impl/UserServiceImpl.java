package com.traffic.client.application.impl;

import com.traffic.client.application.UserService;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.repository.ClientModuleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    private ClientModuleRepository clientModuleRepository;

    @Override
    public void registerUser(User user) {

        try{
            clientModuleRepository.createUser(user);

        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public Optional<List<User>> showUsers() {
        return  clientModuleRepository.listUsers();
    }
}
