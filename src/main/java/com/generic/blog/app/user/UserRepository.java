package com.generic.blog.app.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    public Optional<UserEntity> findByUsername(String username);

    @Override
    Page<UserEntity> findAll(Pageable pageable);

    Page<UserEntity> findUserEntitiesByUsernameContains(String username, Pageable pageable);
}
