package com.gym.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.entity.User;

public interface UserCredentialRepository  extends JpaRepository<User,Integer> {
	Optional<User> findByUserName(String userName);
}
