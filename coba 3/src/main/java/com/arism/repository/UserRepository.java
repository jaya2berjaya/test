package com.arism.repository;

import com.arism.model.Comment;
import com.arism.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // additional method
    Optional<User> findByEmail(String email);
}
