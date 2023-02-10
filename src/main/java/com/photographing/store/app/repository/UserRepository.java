    package com.photographing.store.app.repository;

import com.photographing.store.app.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// This interface interacts with database for data persistence
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    // Find user details from database by username and password
    User findByUsernameAndPassword(String username, String password);

    // This method fetches all the users of a particular role
    List<User> findAllByRole(String role);

}
