package com.gym.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gym.dto.DetailsDTO;
import com.gym.dto.request.TrainerRegistrationDTO;
import com.gym.dto.request.TrainerTrainingsDTO;
import com.gym.dto.request.UpdateTrainerProfileDTO;
import com.gym.dto.response.TraineeDetails;
import com.gym.dto.response.TrainerProfile;
import com.gym.dto.response.TrainerProfileResponseDTO;
import com.gym.dto.response.TrainerTrainingTypeResponseDTO;
import com.gym.exceptions.TraineeException;
import com.gym.exceptions.TrainerException;
import com.gym.exceptions.UserException;
import com.gym.model.Trainee;
import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.model.User;
import com.gym.repository.TraineeRepository;
import com.gym.repository.TrainerRepository;
import com.gym.repository.TrainingRepository;
import com.gym.repository.TrainingTypeRepository;
import com.gym.repository.UserRepository;
import com.gym.service.interfaces.TrainerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {
	@Autowired
	private TrainerRepository trainerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TrainingRepository trainingRepository;
	@Autowired
	private TraineeRepository traineeRepository;
	@Autowired
	private TrainingTypeRepository trainingTypeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final String TRAINEEEXCEP = "No such user been assigned as trainer";

	@Override
	public DetailsDTO trainerRegistration(TrainerRegistrationDTO trainerRegistrationDTO) {
		String email = trainerRegistrationDTO.getEmail();
		String username = email.substring(0, email.indexOf("@"));
		User user = userRepository.save(User.builder().firstName(trainerRegistrationDTO.getFirstName())
				.lastName(trainerRegistrationDTO.getLastName()).email(trainerRegistrationDTO.getEmail())
				.userName(username).password(passwordEncoder.encode("1234")).build());
		TrainingType trainingtype = trainingTypeRepository
				.findByTrainingTypeName(trainerRegistrationDTO.getSpecialization());
				
		trainerRepository.save(Trainer.builder().trainingType(trainingtype).user(user).build());
		log.info("resgisterd user and training typee successfully..");
		return new DetailsDTO(username, user.getPassword());
	}

	@Override
	public TrainerProfileResponseDTO getTrainerProfile(String userName) {
		log.info("User retrieved {}", userName);
		Trainer trainer = Optional.ofNullable(trainerRepository.findByUserUserName(userName))
				.orElseThrow(() -> new TrainerException(TRAINEEEXCEP));
		log.info("Trainer retrieved {}", trainer);
		Set<Trainee> trainees = trainer.getTrainees();
		List<TraineeDetails> traineeDetails = new ArrayList<>();
		trainees.stream().forEach(trainee->{
			User traineeUser = trainee.getUser();
			traineeDetails.add(TraineeDetails.builder().firstName(traineeUser.getFirstName())
					.lastName(traineeUser.getLastName()).userName(traineeUser.getUserName()).build());
		});
		return TrainerProfileResponseDTO.builder().username(trainer.getUser().getUserName())
				.firstName(trainer.getUser().getFirstName()).lastName(trainer.getUser().getLastName())
				.isactive(trainer.getUser().isActive()).specialization(trainer.getTrainingType().getTrainingTypeName())
				.trainees(traineeDetails).build();
	}

	@Override
	public TrainerProfileResponseDTO updateTrainerProfile(UpdateTrainerProfileDTO profileDTO) {
		Trainer trainer = Optional.ofNullable(trainerRepository.findByUserUserName(profileDTO.getUserName()))
				.orElseThrow(() -> new TrainerException(TRAINEEEXCEP));
		trainer.getUser().setActive(profileDTO.isActive());
		trainer.getUser().setLastName(profileDTO.getLastName());
		trainer.getUser().setFirstName(profileDTO.getFirstName());
		trainer.getTrainingType().setTrainingTypeName(profileDTO.getSpecialization());
		trainerRepository.save(trainer);
		log.info("trainer updated {}", trainer.getUser());
		Set<Trainee> trainees = trainer.getTrainees();
		List<TraineeDetails> traineeDetails = new ArrayList<>();
		trainees.stream().forEach(trainee->{
			User traineeUser = trainee.getUser();
			traineeDetails.add(TraineeDetails.builder().firstName(traineeUser.getFirstName())
					.lastName(traineeUser.getLastName()).userName(traineeUser.getUserName()).build());
		});
		return TrainerProfileResponseDTO.builder().username(trainer.getUser().getUserName())
				.firstName(trainer.getUser().getFirstName()).lastName(trainer.getUser().getLastName())
				.isactive(trainer.getUser().isActive()).specialization(trainer.getTrainingType().getTrainingTypeName())
				.trainees(traineeDetails).build();
	}

	@Override
	public List<TrainerProfile> getNotAssignedTrainees(String userName) {
		Trainee trainee = Optional.ofNullable(traineeRepository.findByUserUserName(userName))
				.orElseThrow(() -> new TraineeException(TRAINEEEXCEP));
		log.info("fetched trainee from username {}",userName);
		return trainerRepository.findAll().stream().filter(trainer -> !trainer.getTrainees().contains(trainee))
				.map(userTrainer -> TrainerProfile.builder().firstName(userTrainer.getUser().getFirstName()).lastName(userTrainer.getUser().getLastName())
						.userName(userTrainer.getUser().getUserName()).specialization(userTrainer.getTrainingType().getTrainingTypeName())
						.build())
				.toList();
	}

	@Override
	public List<TrainerTrainingTypeResponseDTO> getTrainerTrainings(TrainerTrainingsDTO traineeTraining)
			throws UserException, TrainerException {
		Trainer trainer = Optional.ofNullable(trainerRepository.findByUserUserName(traineeTraining.getUserName()))
				.orElseThrow(() -> new TrainerException(TRAINEEEXCEP));
		log.info("trainee obtained from db of userName {}", trainer.getUser().getUserName());
		return trainingRepository.findAllByTrainerId(trainer.getId()).stream().map(training->
		 TrainerTrainingTypeResponseDTO.builder().duration(training.getDuration())
				.traineeName(trainer.getUser().getUserName()).trainingDate(training.getDate())
				.trainingName(training.getName()).trainingType(training.getTrainingType().getTrainingTypeName())
				.build()).toList();
	}
	
	@Override
	public String getTrainerEmail(String userName) throws TrainerException{
		Trainer trainer = Optional
				.ofNullable(trainerRepository.findByUserUserName(userName))
				.orElseThrow(() -> new TrainerException("No such user been assigned as trainee"));
		return trainer.getUser().getEmail();
	}
}
