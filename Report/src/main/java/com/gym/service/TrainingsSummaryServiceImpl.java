package com.gym.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.dto.TrainingsSummaryDTO;
import com.gym.model.TrainingsSummary;
import com.gym.repository.TrainingsSummaryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainingsSummaryServiceImpl implements TrainingsSummaryService {

	@Autowired
	private TrainingsSummaryRepository trainingsSummaryRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<TrainingsSummary> getTrainingsSummary() {
		return trainingsSummaryRepository.findAll();
	}

	@Override
	public void addAllTrainingsSummary(List<TrainingsSummaryDTO> trainings) {
		trainings.stream().forEach(
				training -> trainingsSummaryRepository.save(modelMapper.map(training, TrainingsSummary.class)));
		log.info("saved all tarinings into mongodb");
	}

	
	@Override
//	@KafkaListener(topics = "reports", groupId="gymGrp")
	public void addTrainingsSummary(TrainingsSummaryDTO training) {
		trainingsSummaryRepository.save(modelMapper.map(training, TrainingsSummary.class));
		log.info("saved tarining into mongodb");
	}

}
