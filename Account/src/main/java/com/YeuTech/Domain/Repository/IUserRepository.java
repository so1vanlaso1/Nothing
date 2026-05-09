package com.YeuTech.Domain.Repository;

import com.YeuTech.Domain.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();

    void save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailNormalized(String email);
}
