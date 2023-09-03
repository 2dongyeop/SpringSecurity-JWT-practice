package io.dongyeop.springsecurityjwt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authority")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;

    @Builder.Default
    @OneToMany(mappedBy = "authority")
    private Set<UserAuthority> userAuthorities = new HashSet<>();
}