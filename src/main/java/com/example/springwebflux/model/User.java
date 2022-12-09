package com.example.springwebflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("userinfo")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;
    private String givenName;
    private String surName;
    private String userName;
    private String email;
    private String password;


}
