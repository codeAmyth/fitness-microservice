package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            User existingUser = userRepository.findByEmail(request.getEmail());

            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(existingUser.getUserId());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());

            return userResponse;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());

       User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(savedUser.getUserId());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    }

    @Override
    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
//        UserResponse userResponse = new UserResponse();
//
//        userResponse.setUserId(user.getUserId());
//        userResponse.setPassword(user.getPassword());
//        userResponse.setEmail(user.getEmail());
//        userResponse.setFirstName(user.getFirstName());
//        userResponse.setLastName(user.getLastName());
//        userResponse.setCreatedAt(user.getCreatedAt());
//        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    @Override
    public Boolean existByUserId(String userId) {
        return userRepository.existsByKeycloakId(userId);
    }
}
