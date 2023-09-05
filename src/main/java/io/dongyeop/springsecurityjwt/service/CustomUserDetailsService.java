package io.dongyeop.springsecurityjwt.service;

import io.dongyeop.springsecurityjwt.entity.User;
import io.dongyeop.springsecurityjwt.repository.AuthorityRepository;
import io.dongyeop.springsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없음"));
    }

    private org.springframework.security.core.userdetails.User createUser(final String username, final User user) {

        if (!user.isActivated()) {
            throw new RuntimeException("활성화되어있지 않은 사용자");
        }

        final List<SimpleGrantedAuthority> grantedAuthorities = user.getUserAuthorities()
                .stream()
                .map(userAuthority ->
                        new SimpleGrantedAuthority(authorityRepository
                                .findByAuthorityName(userAuthority.getAuthority().getAuthorityName())
                                .orElseThrow().getAuthorityName())).toList();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}
