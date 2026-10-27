package com.example.demo.services.auth;


import com.example.demo.model.Authority;
import com.example.demo.model.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserInfoDetails implements UserDetails {

    private final String username; // Changed from 'name' to 'email' for clarity
    private final String password;
    private final List<? extends GrantedAuthority>  authorities;

    public UserInfoDetails(User user) {
        this.username = user.getUsername(); // Use email as username
        this.password = user.getPassword();
        this.authorities = user.getAuthorities().stream().map(it -> new SimpleGrantedAuthority(it.getName()) ).toList();
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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