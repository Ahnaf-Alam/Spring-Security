package com.example.springsecurityjpa.services;

import com.example.springsecurityjpa.Dao.UserDao;
import com.example.springsecurityjpa.entity.User;
import com.example.springsecurityjpa.util.UserDetailsConverterUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserInfoDetailsService implements UserDetailsService {
    @Inject
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDao.findByUserName(userName);
        UserDetailsConverterUtil userDetailsConverterUtil = new UserDetailsConverterUtil(user);

        return userDetailsConverterUtil;
    }
}
