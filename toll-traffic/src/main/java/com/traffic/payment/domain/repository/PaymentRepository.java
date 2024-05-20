package com.traffic.payment.domain.repository;


import com.traffic.payment.domain.user.User;

import java.util.ArrayList;

public interface PaymentRepository{

    public void initialize();

    public void addUser(User userToAdd);

    public ArrayList<User> getAllUsers();
}
