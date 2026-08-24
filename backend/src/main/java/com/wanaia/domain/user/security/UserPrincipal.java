package com.wanaia.domain.user.security;

import com.wanaia.domain.user.model.User;
import com.wanaia.domain.user.model.UserRole;
import com.wanaia.domain.user.model.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UserPrincipal implements UserDetails {

    private final Long id;
    private final UUID uuid;
    private final String email;
    private final String password;
    private final UserRole role;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, UUID uuid, String email, String password, UserRole role, boolean active,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.uuid = uuid;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        return new UserPrincipal(
            user.getId(),
            user.getUuid(),
            user.getEmail(),
            user.getPasswordHash(),
            user.getRole(),
            user.getStatus() == UserStatus.ACTIVE,
            Collections.singletonList(authority)
        );
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
