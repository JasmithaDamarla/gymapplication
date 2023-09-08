package com.gym.service.implementation;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.dto.TrainingsSummaryDTO;
import com.gym.dto.request.TrainingRequestDTO;
import com.gym.exceptions.TraineeException;
import com.gym.exceptions.TrainerException;
import com.gym.exceptions.TrainingException;
import com.gym.exceptions.UserException;
import com.gym.model.Trainee;
import com.gym.model.Trainer;
import com.gym.model.Training;
import com.gym.repository.TraineeRepository;
import com.gym.repository.TrainerRepository;
import com.gym.repository.TrainingRepository;
import com.gym.service.interfaces.TrainingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

	@Autowired
	private TrainingRepository trainingRepository;
	@Autowired
	private TraineeRepository traineeRepository;
	@Autowired
	private TrainerRepository trainerRepository;
	
	private static final String TRAINEE_EXP ="No such trainee Found";
	
	@Override
	public void addTraining(TrainingRequestDTO trainingRequestDTO)
			throws TrainingException, UserException, TraineeException, TrainerException {

		Trainee trainee = Optional
				.ofNullable(traineeRepository.findByUserUserName(trainingRequestDTO.getTraineeUserName()))
				.orElseThrow(() -> new TraineeException(TRAINEE_EXP));
		log.info("trainee obtained from repository {}", trainee);
		Trainer trainer = Optional
				.ofNullable(trainerRepository.findByUserUserName(trainingRequestDTO.getTrainerUserName()))
				.orElseThrow(() -> new TrainerException("No such trainer Found"));
		log.info("trainer obtained from repository {}", trainer);
//		TrainingType type = trainingTypeRepository.findByTrainingTypeName(trainingRequestDTO.getTrainingType())
//				.orElseThrow(() -> new TrainingException("No such training found"));
//		log.info("training type obtained from repository {}", type);
		trainer.getTrainees().add(trainee);
		trainerRepository.save(trainer);
		trainingRepository.save(Training.builder().date(trainingRequestDTO.getTrainingDate())
				.duration(trainingRequestDTO.getTraningDuration()).name(trainingRequestDTO.getTrainingName())
				.trainingType(trainer.getTrainingType()).trainee(trainee).trainer(trainer).build());
		log.info("training info saved");

		
	}
	
	@Override
	public TrainingsSummaryDTO getTrainings(String trainerUserName,LocalDate date) {
	
		Trainer trainer = Optional
				.ofNullable(trainerRepository.findByUserUserName(trainerUserName))
				.orElseThrow(() -> new TrainerException(TRAINEE_EXP));
		log.info("trainer obtained from repository {}", trainer);

		return TrainingsSummaryDTO.builder().trainerFirstName(trainer.getUser().getFirstName())
		.trainerLastName(trainer.getUser().getLastName()).trainerUserName(trainer.getUser().getUserName())
		.trainerStatus(trainer.getUser().isActive()?"ACTIVE":"INACTIVE")
		.date(date).build();
	}
	
}
