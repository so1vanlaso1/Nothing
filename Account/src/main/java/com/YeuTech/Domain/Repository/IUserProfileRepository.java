package com.YeuTech.Domain.Repository;

import java.util.Optional;
import com.YeuTech.Domain.Entities.UserProfile;

public interface IUserProfileRepository {
    void save(UserProfile userProfile);
    Optional<UserProfile> findByUserId(String userId);
}
