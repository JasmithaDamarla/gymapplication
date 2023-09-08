package com.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.model.TrainingType;

public interface TrainingTypeRepository extends JpaRepository<TrainingType,Integer>{
	TrainingType findByTrainingTypeName(String trainingTypeName);
}
