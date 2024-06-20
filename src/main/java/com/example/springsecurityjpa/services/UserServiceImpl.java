package com.example.springsecurityjpa.services;

import com.example.springsecurityjpa.Dao.UserDao;
import com.example.springsecurityjpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        User newUser = null;
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("ROLE_" + user.getRole());
            newUser = userDao.saveAndFlush(user);
        } catch (Exception e){

        }
        return newUser;
    }

    @Override
    public List<User> getAllUser() throws Exception {
        return userDao.findAll();
    }
}
