package com.scaler.userservice.repositories;

import com.scaler.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Optional is use to avoid null values
    Optional<User> findByEmail(String email);

    @Override
    User save(User user);



}
