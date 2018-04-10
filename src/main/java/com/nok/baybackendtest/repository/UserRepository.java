package com.nok.baybackendtest.repository;

import com.nok.baybackendtest.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    public Optional<UserEntity> findByUsername(String username);
}