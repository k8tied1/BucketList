package com.techelevator.dao;

import com.techelevator.model.User;

import java.util.List;

public interface UserDao {


    User getUserById(Long userId);

    List<User> findAll(int currentLoggedInUser);

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password, String role, String emial);


    List<User> findAllAccessToMe(int idByUsername);
}
