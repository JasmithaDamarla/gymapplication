package com.gym.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.dto.DetailsDTO;
import com.gym.dto.request.TraineeProfileRequestDTO;
import com.gym.dto.request.TraineeRegistrationDTO;
import com.gym.dto.request.TraineeTrainersDTO;
import com.gym.dto.request.TraineeTrainingsDTO;
import com.gym.dto.request.UpdateTraineeDTO;
import com.gym.dto.response.TraineeProfileResponseDTO;
import com.gym.dto.response.TraineeTrainingTypeResponseDTO;
import com.gym.dto.response.TrainerProfile;
import com.gym.exceptions.TraineeException;
import com.gym.exceptions.TrainerException;
import com.gym.exceptions.TrainingTypeException;
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
import com.gym.service.interfaces.TraineeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TraineeRepository traineeRepository;
	@Autowired
	private TrainerRepository trainerRepository;
	@Autowired
	private TrainingRepository trainingRepository;
	@Autowired
	private TrainingTypeRepository trainingTypeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public DetailsDTO traineeRegistration(TraineeRegistrationDTO traineeRegistrationDTO) {

		String email = traineeRegistrationDTO.getEmail();
		String username = email.substring(0, email.indexOf("@"));
		User user = userRepository.save(User.builder().firstName(traineeRegistrationDTO.getFirstName())
				.lastName(traineeRegistrationDTO.getLastName()).password(passwordEncoder.encode("1234"))
				.email(traineeRegistrationDTO.getEmail()).userName(username).build());
		traineeRepository.save(Trainee.builder().address(traineeRegistrationDTO.getAddress())
				.dob(traineeRegistrationDTO.getDob()).user(user).build());
		log.info("trainee being registered");
		return new DetailsDTO(username, user.getPassword());
	}

	@Override
	public TraineeProfileResponseDTO getTraineeProfile(String userName) throws UserException, TrainingTypeException {

		Trainee trainee = Optional.ofNullable(traineeRepository.findByUserUserName(userName))
				.orElseThrow(() -> new TraineeException("No trainee found with userName = "+userName));
		List<Trainer> trainers = trainee.getTrainers();
		List<TrainerProfile> trainerprofile = new ArrayList<>();
		log.info("retrieved trainee ");
		trainers.stream().forEach(tr -> {
			User trainerUser = userRepository.findById(tr.getUser().getId()).get();
//					.orElseThrow(() -> new UserException("No such User found"));
			log.info("user retrieved {}", trainerUser);
			TrainingType type = trainingTypeRepository.findById(tr.getTrainingType().getId())
					.orElseThrow(() -> new TrainingTypeException("No such training found"));
			log.info("trainingType retrieved {}", trainerUser);
			trainerprofile.add(
					TrainerProfile.builder().firstName(trainerUser.getFirstName()).userName(trainerUser.getUserName())
							.lastName(trainerUser.getLastName()).specialization(type.getTrainingTypeName()).build());
		});
		return TraineeProfileResponseDTO.builder().userName(userName).firstName(trainee.getUser().getFirstName())
				.lastName(trainee.getUser().getLastName()).dob(trainee.getDob()).trainersList(trainerprofile)
				.address(trainee.getAddress()).isActive(trainee.getUser().isActive()).build();
	}

	@Override
	public TraineeProfileResponseDTO updateTraineeProfile(TraineeProfileRequestDTO profileDTO)
			throws UserException, TrainingTypeException {
		User user = userRepository.findByUserName(profileDTO.getUserName())
				.orElseThrow(() -> new UserException("No such user found"));
		user.setActive(profileDTO.isActive());
		user.setFirstName(profileDTO.getFirstName());
		user.setLastName(profileDTO.getLastName());
		user.setUserName(profileDTO.getUserName());
		userRepository.save(user);
		log.info("user repository updated");

		Trainee trainee = traineeRepository.findByUserUserName(user.getUserName());
		trainee.setAddress(profileDTO.getAddress());
		trainee.setDob(profileDTO.getDob());
		traineeRepository.save(trainee);
		log.info("trainee repository updated");

		List<Trainer> trainers = trainee.getTrainers();
		TraineeProfileResponseDTO profile = TraineeProfileResponseDTO.builder().userName(user.getUserName())
				.firstName(user.getFirstName()).lastName(user.getLastName()).dob(trainee.getDob())
				.address(trainee.getAddress()).isActive(user.isActive()).build();
		List<TrainerProfile> trainerprofile = new ArrayList<>();
		trainers.stream().forEach(tr -> {
			User trainerUser = userRepository.findById(tr.getUser().getId()).get();
//					.orElseThrow(() -> new UserException("No such User found"));
			TrainingType type = trainingTypeRepository.findById(tr.getTrainingType().getId())
					.orElseThrow(() -> new TrainingTypeException("No such training found"));
			trainerprofile.add(
					TrainerProfile.builder().firstName(trainerUser.getFirstName()).userName(trainerUser.getUserName())
							.lastName(trainerUser.getLastName()).specialization(type.getTrainingTypeName()).build());
		});
		profile.setTrainersList(trainerprofile);
		return profile;
	}

	@Transactional
	@Override
	public void deleteByUserName(String userName) throws TraineeException {
		Trainee trainee = Optional.ofNullable(traineeRepository.findByUserUserName(userName))
				.orElseThrow(() -> new TraineeException("No such trainee found"));
		log.info("trainee obtained");
		trainingRepository.deleteByTraineeId(trainee.getId());
		log.info("training repository cleared");
		trainee.getTrainers().stream().forEach(trainer -> {
			trainer.getTrainees().remove(trainee);
			trainerRepository.save(trainer);
		});
		traineeRepository.deleteById(trainee.getId());
		log.info("trainee repository cleared");
		userRepository.deleteByUserName(userName);
		log.info("user deleted successfully");
	}

	@Override
	public List<TrainerProfile> updateTrainers(UpdateTraineeDTO trainee) throws UserException, TrainerException {
		Trainee updateTrainee = Optional.ofNullable(traineeRepository.findByUserUserName(trainee.getUserName()))
				.orElseThrow(() -> new TraineeException("No such trainee found"));
		List<Trainer> trainersList = updateTrainee.getTrainers();
		List<TrainerProfile> trainers = new ArrayList<>();
		trainee.getTrainers().stream().forEach(profile -> {
			Trainer trainer = Optional.ofNullable(trainerRepository.findByUserUserName(profile))
					.orElseThrow(() -> new TrainerException("No user been assigned as trainer"));
			trainersList.add(trainer);
			trainers.add(TrainerProfile.builder().firstName(trainer.getUser().getFirstName())
					.lastName(trainer.getUser().getLastName()).userName(trainer.getUser().getUserName())
					.specialization(trainer.getTrainingType().getTrainingTypeName()).build());
		});
		updateTrainee.setTrainers(trainersList);
		traineeRepository.save(updateTrainee);
		log.info("updated trainee successfully with trainers");
		return trainers;
	}

	@Override
	public List<TraineeTrainingTypeResponseDTO> getTraineeTrainings(TraineeTrainingsDTO traineeTraining)
			throws UserException, TrainerException {
		return trainingRepository
				.findTrainingsForTrainee(traineeTraining.getUserName(), traineeTraining.getPeriodFrom(),
						traineeTraining.getPeriodTo(), traineeTraining.getTrainerName(),
						traineeTraining.getTrainingType())
				.stream()
				.map(training -> TraineeTrainingTypeResponseDTO.builder().duration(training.getDuration())
						.trainerName(training.getTrainer().getUser().getUserName()).trainingDate(training.getDate())
						.trainingName(training.getName()).trainingType(training.getTrainingType().getTrainingTypeName())
						.build())
				.toList();
	}

	@Transactional
	@Override
	public List<TrainerProfile> updateTraineesTrainers(TraineeTrainersDTO traineeTrainersDTO) {
		Trainee trainee = Optional
				.ofNullable(traineeRepository.findByUserUserName(traineeTrainersDTO.getTraineeUserName()))
				.orElseThrow(() -> new TraineeException("No such user been assigned as trainee"));

		List<Trainer> trainers = traineeTrainersDTO.getTrainerUserNames().stream()
				.map(trainerUserName -> Optional.ofNullable(trainerRepository.findByUserUserName(trainerUserName))
						.orElseThrow(() -> new TrainerException("No such trainer found")))
				.toList();

		trainee.getTrainers().stream().forEach(trainer -> trainer.getTrainees().remove(trainee));
		trainers.stream().forEach(trainer -> trainer.getTrainees().add(trainee));
		traineeRepository.save(trainee);
		log.info("updated trainee trainers successfully");
		return trainers.stream()
				.map(trainer -> TrainerProfile.builder().firstName(trainer.getUser().getUserName())
						.lastName(trainer.getUser().getLastName()).userName(trainer.getUser().getUserName())
						.specialization(trainer.getTrainingType().getTrainingTypeName()).build())
				.toList();
	}

	@Override
	public String getTraineeEmail(String userName) throws TraineeException {
		Trainee trainee = Optional.ofNullable(traineeRepository.findByUserUserName(userName))
				.orElseThrow(() -> new TraineeException("No such user been assigned as trainee"));
		return trainee.getUser().getEmail();
	}
}
