package com.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.Trainee;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
	Trainee findByUserUserName(String userName);

	
}
