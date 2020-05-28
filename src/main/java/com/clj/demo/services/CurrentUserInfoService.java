package com.clj.demo.services;

import com.clj.demo.entity.User;
import com.clj.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserInfoService {

    @Autowired
    private UserRepository userRepository;

    public User getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findUserByEmail( principal.toString());
        return user.orElseGet(User::new);
    }
}