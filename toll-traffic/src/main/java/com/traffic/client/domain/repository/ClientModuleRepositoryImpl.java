package com.traffic.client.domain.repository;

import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.Tag;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


//Implementar metodos de comunicacion con bd
@ApplicationScoped
public class ClientModuleRepositoryImpl implements ClientModuleRepository{

    @Override
    public User findByTag(Tag tag) {
        return null;
    }

    @Override
    public List<User> listUsers() {
        return List.of();
    }

    @Override
    public User createUser(User user) {
        return null;
    }
}
