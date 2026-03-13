package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.User;

import java.util.Optional;

public interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<com.project.EzSplit_Backend.Entity.User, Long> {
    Optional<User> findByUsername(String username);
}
