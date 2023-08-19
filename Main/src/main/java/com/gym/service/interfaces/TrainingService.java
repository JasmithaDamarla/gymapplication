package com.gym.service.interfaces;

import java.time.LocalDate;

import com.gym.dto.TrainingsSummaryDTO;
import com.gym.dto.request.TrainingRequestDTO;

public interface TrainingService {

	void addTraining(TrainingRequestDTO trainingRequestDTO);
	TrainingsSummaryDTO getTrainings(String trainerUserName, LocalDate date);
}
