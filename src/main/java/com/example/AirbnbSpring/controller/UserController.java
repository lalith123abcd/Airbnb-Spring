package com.example.AirbnbSpring.controller;

import com.example.AirbnbSpring.dto.UserRequestDTO;
import com.example.AirbnbSpring.dto.UserResponseDTO;
import com.example.AirbnbSpring.model.User;
import com.example.AirbnbSpring.services.IUserService;
import com.example.AirbnbSpring.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final IUserService userService;
    @PostMapping

    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO dto){

        UserResponseDTO response = userService.createUser(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }
}
