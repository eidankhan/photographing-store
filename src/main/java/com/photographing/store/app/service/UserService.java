package com.photographing.store.app.service;

import com.photographing.store.app.modal.User;
import org.springframework.stereotype.Service;

import java.util.List;


// This interface defines all the methods for persisting users
@Service
public interface UserService {
    public User findById(Long id);

    public User save(User user);

    public User findByUsername(String username);

    public User findByUsernameAndPassword(String username, String password);

    List<User> findAllByRole(String role);
}
