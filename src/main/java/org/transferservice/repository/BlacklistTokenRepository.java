package org.transferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transferservice.model.BlacklistToken;

import java.util.Optional;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
    Optional<BlacklistToken> findByToken(String token);
}