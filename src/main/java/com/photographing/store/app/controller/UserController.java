package com.photographing.store.app.controller;

import com.photographing.store.app.dto.UserDTO;
import com.photographing.store.app.modal.User;
import com.photographing.store.app.service.UserService;
import com.photographing.store.app.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    // Injecting UserService by using @Autowired annotation

    @Autowired
    private UserService userService;


    // This endpoint fetches user details by id
    @GetMapping("/{userId}")
    public GenericResponse getUserDetailsById(@PathVariable Long userId){
        User userById = userService.findById(userId);
        // Check if user exists in the database, if yes return its details otherwise return User not found
        if(userById != null)
            return new GenericResponse("Success","User exists", userById);
        return new GenericResponse("Failure","User not found");
    }

    // This endpoint fetches all the photos of a customer
    @GetMapping("/photos/{id}")
    public User getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    // This endpoint registers new customers in the photo store
    @PostMapping("/registration")
    public GenericResponse register(@RequestBody User user) {
        // Saving new customer in the database
        User savedUser = userService.save(user);
        // Check if customer is saved in the database or not
        if (savedUser != null)
            return new GenericResponse("Success", "New user is registered successfully", savedUser);
        return new GenericResponse("Failure", "Unable to register new user", null);
    }

    // This endpoint authorizes all the users before accessing photo store application
    @PostMapping("/authentication")
    public GenericResponse authenticate(@RequestBody User user){
        System.out.println("Authenticating user ----------------");
        User userByUsername = userService.findByUsername(user.getUsername());
        // Check if username exists in the database or not
        if(userByUsername == null)
            return new GenericResponse("Failure", "User not found");
        // Authenticate user by username and password
        User userByUsernameAndPassword = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        System.out.println("userByUsernameAndPassword ->"+userByUsernameAndPassword == null);

        // Check if the credentials of the user are correct
        if(userByUsernameAndPassword == null)
            return new GenericResponse("Failure", "Incorrect username or password");
        return new GenericResponse("Success", "You have successfully logged in", userByUsernameAndPassword);
    }

    // This endpoint returns all the albums that are available in the database of photo store system
    @GetMapping("/albums")
    public GenericResponse getAlbumsList(){
        List<UserDTO> albums = new ArrayList<UserDTO>();
        List<User> customers = userService.findAllByRole("Customer");
        // Populate albums details in the albums list
        for(User user : customers){
            // Adding album in albums List
            albums.add(new UserDTO(user.getId(), user.getName()));
        }
        // Return Generic Response with albums data
        return new GenericResponse("Success", "Albums", albums);
    }
}
