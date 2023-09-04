package io.dongyeop.springsecurityjwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_name")
    private Authority authority;

    private void setUser(final User user) {
        this.user = user;
        user.getUserAuthorities().add(this);
    }

    private void setAuthority(final Authority authority) {
        this.authority = authority;
        authority.getUserAuthorities().add(this);
    }

    public UserAuthority(final User user, final Authority authority) {
        this.setUser(user);
        this.setAuthority(authority);
    }
}
