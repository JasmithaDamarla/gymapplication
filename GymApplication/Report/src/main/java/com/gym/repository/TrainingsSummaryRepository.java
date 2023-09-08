package com.gym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gym.model.TrainingsSummary;

public interface TrainingsSummaryRepository extends MongoRepository<TrainingsSummary, String>{

	TrainingsSummary findByTrainerUserName(String trainerUserName);

}
