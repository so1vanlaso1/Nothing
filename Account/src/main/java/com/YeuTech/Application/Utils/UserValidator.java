package com.YeuTech.Application.Utils;

import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Repository.IUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {
    private final IUserRepository userRepository;

    public UserValidator(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String checkEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already exists!";
        }
        return null;
    }

    public Optional<User> findByEmailNormalized(String email) {
        return userRepository.findByEmailNormalized(email.toLowerCase().trim());
    }

}
