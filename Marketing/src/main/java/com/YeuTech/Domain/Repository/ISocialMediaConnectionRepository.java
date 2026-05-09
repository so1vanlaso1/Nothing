package com.YeuTech.Domain.Repository;

import java.util.Optional;
import com.YeuTech.Domain.Entities.SocialMediaConnection;
import com.YeuTech.Domain.Enums.SocialPlatform;

public interface ISocialMediaConnectionRepository {
    Optional<SocialMediaConnection> findByUserIdAndPlatform(String userId, SocialPlatform platform);

    void save(SocialMediaConnection connection);
}
