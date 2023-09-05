package io.dongyeop.springsecurityjwt.repository;

import io.dongyeop.springsecurityjwt.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Username으로 User 조회시 EAGER 방식으로 authorities 정보도 함께 조회
     *
     * @param username
     * @return
     */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(final String username);
}
