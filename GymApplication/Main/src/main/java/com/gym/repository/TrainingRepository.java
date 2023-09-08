package com.gym.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gym.model.Trainee;
import com.gym.model.Trainer;
import com.gym.model.Training;

public interface TrainingRepository extends JpaRepository<Training,Integer>{
	Training findByTrainer(Trainer trainer);
	Training findByTrainee(Trainee trainee);
	void deleteByTraineeId(int id);
	Training findByTraineeId(int id);
	Training findByTrainerId(int id);
	List<Training> findAllByTraineeId(int id);
	List<Training> findAllByTrainerId(int id);
	
	@Query("SELECT t FROM Training t " + "JOIN t.trainee tr " + "JOIN t.trainer tnr " + "JOIN t.trainingType tt "
			+ "WHERE tr.user.userName = :username " + "AND (:periodFrom IS NULL OR t.date >= :periodFrom) "
			+ "AND (:periodTo IS NULL OR t.date <= :periodTo) "
			+ "AND (:trainerName IS NULL OR tnr.user.userName LIKE %:trainerName%) "
			+ "AND (:trainingType IS NULL OR tt.trainingTypeName LIKE %:trainingType%)")
	List<Training> findTrainingsForTrainee(String username, LocalDate periodFrom, LocalDate periodTo,
			String trainerName, String trainingType);
	
}
