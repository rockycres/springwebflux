package com.example.springwebflux.controller;

import com.example.springwebflux.model.User;
import com.example.springwebflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("spring-webflux")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Flux<User> findAllUsers(){
        return userService.getUsers();
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<User> findUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping(value = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody User user){
        userService.addUser(user);
    }


    @PostMapping(value = "/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

}
