package com.generic.blog.app.auth.models;

import com.generic.blog.app.auth.EnumRole;
import com.generic.blog.app.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "\"User\"",
        uniqueConstraints=@UniqueConstraint(columnNames = {"username"})
)
public class AuthUser implements UserDetails {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name="display_name", nullable = false)
    private String displayName;

    @Column(name="role", nullable = false)
    private EnumRole role;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToMany()
    @JoinTable(name = "subscriptions", joinColumns =
            {@JoinColumn(name="subscriber_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "subscription_id", referencedColumnName = "id")})
    private Set<UserEntity>subscriptions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role.getRole()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
