package com.example.springwebflux.service;

import com.example.springwebflux.model.User;
import com.example.springwebflux.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Mono<User> getUserById(Long id){
        return userRepository.findById(id).switchIfEmpty(monoResponseStatusException());
    }

    private Mono<? extends User> monoResponseStatusException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    }


    public Flux<User> getUsers(){
        return userRepository.findAll().delayElements(Duration.ofSeconds(3));
    }

    public void addUser(User user){
        userRepository.save(user).subscribe();
    }


    public Mono<User> updateUser(User user){
        return userRepository.findById(user.getId()).switchIfEmpty(Mono.error(new Exception("User Not Found")))
                .map(oldUser -> {
                    if(user.getUserName() !=null) oldUser.setUserName(user.getUserName());
                    if(user.getSurName() !=null) oldUser.setSurName(user.getSurName());
                    if(user.getGivenName() !=null) oldUser.setGivenName(user.getGivenName());
                    if(user.getEmail() !=null) oldUser.setEmail(user.getEmail());
                    if(user.getPassword() !=null) oldUser.setPassword(user.getPassword());
                    return oldUser;
                }).flatMap(userRepository::save);
    }


    public Mono<Void> deleteUser(Long id){
        return userRepository.deleteById(id).switchIfEmpty(Mono.error(new Exception("User Not Found")));
    }
}
