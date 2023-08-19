package com.gym.service;

import java.util.List;

import com.gym.dto.TrainingsSummaryDTO;
import com.gym.model.TrainingsSummary;

public interface TrainingsSummaryService {

	List<TrainingsSummary> getTrainingsSummary();
	void addAllTrainingsSummary(List<TrainingsSummaryDTO> trainings);
	void addTrainingsSummary(TrainingsSummaryDTO training);
}
