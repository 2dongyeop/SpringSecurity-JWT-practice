package io.dongyeop.springsecurityjwt.repository;

import io.dongyeop.springsecurityjwt.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthorityName(final String authorityName);
}
