package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.dto.UserRequestDTO;
import com.example.AirbnbSpring.dto.UserResponseDTO;
import com.example.AirbnbSpring.model.User;

public interface IUserService {

    UserResponseDTO createUser(UserRequestDTO dto);

}
