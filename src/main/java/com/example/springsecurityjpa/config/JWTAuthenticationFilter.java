package com.example.springsecurityjpa.config;


import com.example.springsecurityjpa.services.JWTUtil;
import com.example.springsecurityjpa.services.UserInfoDetailsService;
import com.example.springsecurityjpa.util.JWTConstants;
import com.example.springsecurityjpa.util.UserDetailsConverterUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserInfoDetailsService userInfoDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader(JWTConstants.HEADER_STRING);
        String username = null;
        String token = null;

        if(requestHeader != null && requestHeader.startsWith(JWTConstants.TOKEN_PREFIX)) {
            token = requestHeader.substring(7);

            try {
                username = this.jwtUtil.getUsernameFormToken(token);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid header value");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // fetch user details from username
            UserDetailsConverterUtil userDetails = (UserDetailsConverterUtil) this.userInfoDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtUtil.validateToken(token, userDetails);

            if(validateToken) {
                // set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Validation Failed!!!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
