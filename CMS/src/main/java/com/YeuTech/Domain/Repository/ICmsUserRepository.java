package com.YeuTech.Domain.Repository;

import java.util.Optional;

public interface ICmsUserRepository {
    Optional<String> findUserIdByEmailNormalized(String emailNormalized);
}
