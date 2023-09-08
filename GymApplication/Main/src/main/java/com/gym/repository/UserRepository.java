package com.gym.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<User> findByUserName(String userName);
	void deleteByUserName(String userName);
}
