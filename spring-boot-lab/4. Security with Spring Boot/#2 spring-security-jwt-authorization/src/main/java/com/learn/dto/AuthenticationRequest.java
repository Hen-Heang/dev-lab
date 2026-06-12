package com.learn.dto;

public record AuthenticationRequest(
        String username,
        String password
){
}
