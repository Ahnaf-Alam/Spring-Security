package com.example.springsecurityjpa.controller;

import com.example.springsecurityjpa.entity.User;
import com.example.springsecurityjpa.models.AuthenticationRequest;
import com.example.springsecurityjpa.models.AuthenticationResponse;
import com.example.springsecurityjpa.services.JWTUtil;
import com.example.springsecurityjpa.services.UserInfoDetailsService;
import com.example.springsecurityjpa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class controller {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserInfoDetailsService userInfoDetailsService;

    @GetMapping("/test1")
    public String test() {
        return "success";
    }

    @GetMapping("/test2")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String test2() {
        return "success-test-2";
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        User newUser = null;
        try {
            newUser = userService.addUser(user);
        } catch (Exception e) {
        }
        return newUser;
    }

    @GetMapping("getUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllUser() {
        List<User> users = null;
        try {
            users = userService.getAllUser();
        } catch (Exception e) {

        }
        return users;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestParam String username, @RequestParam String password) {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        this.doAuthenticate(request.getUserName(), request.getPassword());

        UserDetails userDetails = this.userInfoDetailsService.loadUserByUsername(request.getUserName());
        String token = this.jwtUtil.generateToken(userDetails);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .jwtToken(token).username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String passowrd) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, passowrd);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid Username or Password!!");
        }
    }
}
