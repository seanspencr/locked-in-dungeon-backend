package com.example.demo.repository;

import com.example.demo.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    public Optional<Authority> findByName(String name);
}
