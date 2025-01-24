package com.scaler.userservice.repositories;

import com.scaler.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Override
    Token save(Token token);

    Optional<Token> findByValueAndAndDeleted(String token, Boolean deleted);


    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String token, Boolean deleted, Date expiryAt);
}
