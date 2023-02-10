package com.photographing.store.app.service.impl;

import com.photographing.store.app.modal.User;
import com.photographing.store.app.repository.UserRepository;
import com.photographing.store.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id).get() : null;
    }

    @Override
    public User save(User user) {
        user.setRole("Customer");
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    // This method fetches all the users with the specified role
    @Override
    public List<User> findAllByRole(String role) {
        return userRepository.findAllByRole(role);
    }
}
