package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.dto.UserRequestDTO;
import com.example.AirbnbSpring.dto.UserResponseDTO;
import com.example.AirbnbSpring.model.User;
import com.example.AirbnbSpring.repository.writes.UserWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

private final UserWriteRepository userWriteRepository;
    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // later encode
                .build();

        User savedUser = userWriteRepository.save(user);

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail()).build();
    }


}
