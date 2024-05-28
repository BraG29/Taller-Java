package com.traffic.sucive.domain.repository;

import com.traffic.sucive.domain.user.User;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

public class SuciveRepositoryImplementation  implements SuciveRepository {

    private ArrayList<User> users;

    @PostConstruct
    public void initialize(){
        users = new ArrayList<>();
        //users.add(new User());
    }

    @Override
    public void addUser(User user){
        users.add(user);
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

}
