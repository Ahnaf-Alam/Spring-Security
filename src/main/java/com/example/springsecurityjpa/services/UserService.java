package com.example.springsecurityjpa.services;

import com.example.springsecurityjpa.entity.User;

import java.util.List;

public interface UserService {
    User addUser(User user) throws Exception;
    List<User> getAllUser() throws Exception;
}
