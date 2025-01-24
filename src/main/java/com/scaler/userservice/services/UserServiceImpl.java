package com.scaler.userservice.services;

import com.scaler.userservice.configuration.BcryptPasswordEncoderConfig;
import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.repositories.TokenRepository;
import com.scaler.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public User signUp(String name, String email, String password) {
        // check if email present or not
        // if yes - send them to login page
        // if no - create a user in Db

        //1st thing we need to do is to get the user
        Optional<User> optionalUser  = userRepository.findByEmail(email);

        User user = null;
        if(optionalUser.isPresent()) {
            //Navigate them to login flow
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            // we can not save password directly to DB: - need to use Bcrypt
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user = userRepository.save(user);

        }
        return user;
    }
    private Token createToken(User user) {
        Token token = new Token();
        token.setUser(user);
        // to generate random token
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        // to set expiry date
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        token.setExpiryAt(expiryDate);
        return token;
    }
    @Override
    public Token login(String email, String password) {
        // 1. get the user
        // if not present - navigate to sign up
        // if yes - generate Token
        User user = null;
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            // sign Up flow
        } else{
            user = optionalUser.get();
            // checking if password is correct or not
            if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return null;
            }
            // else it is validated
            Token token = createToken(user);
            token = tokenRepository.save(token);
            return token;
         }
        return null;
    }

    @Override
    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(
                token,
                false,
                new Date()
        );
        // We token not found in database
        // or deleted is true
        // or expiry is not proper
        if(optionalToken.isEmpty()){
            return null;
        }
        Token token1 = optionalToken.get();
        return token1.getUser();
    }

    @Override
    public void logout(String token) {
        // need to check token is valid or deleted
        Optional<Token> optionalToken = tokenRepository.findByValueAndAndDeleted(token, false);

        if(optionalToken.isEmpty()){
            // token is not valid
        } else {
            Token token1 = optionalToken.get();
            token1.setDeleted(true);
            tokenRepository.save(token1);
        }
    }
}
