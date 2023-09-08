package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.gym.model.Trainee;
import com.gym.model.Trainer;
import com.gym.model.Training;
import com.gym.model.TrainingType;
import com.gym.model.User;
import com.gym.repository.TraineeRepository;
import com.gym.repository.TrainerRepository;
import com.gym.repository.TrainingRepository;
import com.gym.repository.TrainingTypeRepository;
import com.gym.repository.UserRepository;
import com.gym.service.implementation.TrainerServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private TraineeRepository traineeRepository;
	@Mock
	private TrainerRepository trainerRepository;
	@Mock
	private TrainingRepository trainingRepository;
	@Mock
	private TrainingTypeRepository trainingTypeRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@InjectMocks
	private TrainerServiceImpl trainerServiceImpl;

	@Test
	void trainerRegistartionSuccess() {
		String generatedPassword = "76AD&^jk";
		TrainerRegistrationDTO trainerRegister = new TrainerRegistrationDTO("ben", "stone", "benStone@gmail.com","YOGA");
		Mockito.when(passwordEncoder.encode(anyString())).thenReturn(generatedPassword);
		User user = User.builder().firstName(trainerRegister.getFirstName()).lastName(trainerRegister.getLastName())
				.password(generatedPassword).email(trainerRegister.getEmail()).userName("benStone").build();
		Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
		DetailsDTO actual = trainerServiceImpl.trainerRegistration(trainerRegister);
		assertEquals(user.getUserName(), actual.getUserName());
		Mockito.verify(passwordEncoder, times(1)).encode(anyString());
	}
	
	@Test
	void updateTrainerProfileSuccess() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainee trainee = new Trainee(1, dob, "Hyd", new ArrayList<>(), userTrainee);
		Trainer trainer = new Trainer(1, Set.of(trainee), userTrainer, new TrainingType(1, "YOGA"));
		List<Trainer> trainers = new ArrayList<>();
		trainers.add(trainer);
		trainee.setTrainers(trainers);
		trainee.setAddress("Hyd");
		List<TraineeDetails> list = new ArrayList<>();
		list.add(TraineeDetails.builder()
				.userName("trainee")
				.firstName("trainee")
				.lastName("trainee")
				.build());
		
		TrainerProfileResponseDTO exp = new TrainerProfileResponseDTO("trainer", "trainer", "trainer","YOGA", true,
				list);
		UpdateTrainerProfileDTO dto = new UpdateTrainerProfileDTO("trainer","trainer", "trainer","YOGA",true);
		Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
		TrainerProfileResponseDTO act = trainerServiceImpl.updateTrainerProfile(dto);
		assertEquals(exp, act);
		
	}
	
	@Test
	void updateTrainerProfileTrainerNotFound() {
	    UpdateTrainerProfileDTO dto = new UpdateTrainerProfileDTO("benstone","ben", "stone","YOGA",true);
	    Mockito.when(trainerRepository.findByUserUserName("benstone")).thenReturn(null);

	    assertThrows(TrainerException.class, () -> {
	        trainerServiceImpl.updateTrainerProfile(dto);
	    });
	    Mockito.verify(trainerRepository, times(1)).findByUserUserName(anyString());
	    Mockito.verify(trainerRepository, never()).save(any(Trainer.class));
	}
	
	@Test
	void getTrainerProfileSuccess() {

		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainee trainee = new Trainee(1, dob, "Hyd", new ArrayList<>(), userTrainee);
		Trainer trainer = new Trainer(1, Set.of(trainee), userTrainer, new TrainingType(1, "YOGA"));
		List<Trainer> trainers = new ArrayList<>();
		trainers.add(trainer);
		trainee.setTrainers(trainers);
		trainee.setAddress("Hyd");
		List<TraineeDetails> list = new ArrayList<>();
		list.add(TraineeDetails.builder()
				.userName("trainee")
				.firstName("trainee")
				.lastName("trainee")
				.build());
		
		TrainerProfileResponseDTO exp = new TrainerProfileResponseDTO("trainer", "trainer", "trainer","YOGA", true,
				list);
		
		Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
		TrainerProfileResponseDTO act = trainerServiceImpl.getTrainerProfile(anyString());
		assertEquals(exp, act);
	}

	@Test
	void getTrainerProfileException() {
		Mockito.when(trainerRepository.findByUserUserName("trainer")).thenReturn(null);
		assertThrows(TrainerException.class, () -> {
			trainerServiceImpl.getTrainerProfile("trainer");
		});

	}

	@Test
	void getNotAssignedTrainersSuccess() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		Trainer trainer1 = new Trainer(1, new HashSet<>(), userTrainer, new TrainingType(1, "YOGA"));
		Trainer trainer2 = new Trainer(2, Set.of(trainee), userTrainer, new TrainingType(1, "YOGA"));
		List<Trainer> trainers = new ArrayList<>();
		trainers.add(trainer1);
		trainers.add(trainer2);
		trainee.setTrainers(trainers);

		List<TrainerProfile> exp = new ArrayList<>();
		exp.add(TrainerProfile.builder().build());
		Mockito.when(trainerRepository.findAll()).thenReturn(trainers);
		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		List<TrainerProfile> act = trainerServiceImpl.getNotAssignedTrainees("trainee");
		assertEquals(exp.size(), act.size());

	}

	@Test
	void getNotAssignedTrinersFail() {
		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(null);
		assertThrows(TraineeException.class, () -> {
			trainerServiceImpl.getNotAssignedTrainees(anyString());
		});
	}

	@Test
	void getTrainerTrainingsSuccess() {
		String username = "ben";
		LocalDate periodFrom = LocalDate.of(2023, 8, 1);
		LocalDate periodTo = LocalDate.of(2023, 8, 10);
		String traineeName = "trainer1";
		String trainingType = "YOGA";

		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		Trainer trainer = new Trainer(1, Set.of(trainee), userTrainer, new TrainingType(1, "YOGA"));
		List<Trainer> trainers = new ArrayList<>();
		trainers.add(trainer);
		trainee.setTrainers(trainers);
		trainee.setAddress("Hyd");
		List<TrainerTrainingTypeResponseDTO> exp = new ArrayList<>();
		exp.add(new TrainerTrainingTypeResponseDTO("Batch", periodFrom, trainingType, 2, username));
		TrainerTrainingsDTO dto = new TrainerTrainingsDTO(username, periodFrom, periodTo, traineeName);
		List<Training> trainings = new ArrayList<>();
		trainings.add(new Training(1, trainee, trainer, "YOGA", new TrainingType(1, "YOGA"), dob, 2));

		Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
		Mockito.when(trainingRepository.findAllByTrainerId(anyInt())).thenReturn(trainings);
		List<TrainerTrainingTypeResponseDTO> act = trainerServiceImpl.getTrainerTrainings(dto);
		assertEquals(exp.size(), act.size());
	}

	@Test
	void getTrainerTrainingsException() {
		String username = "ben";
		LocalDate periodFrom = LocalDate.of(2023, 8, 1);
		LocalDate periodTo = LocalDate.of(2023, 8, 10);
		String traineeName = "trainer1";
		String trainingType = "YOGA";

		LocalDate dob = LocalDate.of(2000, 1, 1);
		TrainerTrainingsDTO dto = new TrainerTrainingsDTO(username, periodFrom, periodTo, traineeName);
		List<Training> trainings = new ArrayList<>();
		trainings.add(new Training(1, new Trainee(), new Trainer(), trainingType, new TrainingType(1, "YOGA"), dob, 2));

		Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(null);
		assertThrows(TrainerException.class, () -> {
			trainerServiceImpl.getTrainerTrainings(dto);
		});
	}

	@Test
	void getTrainerEmailSucess() {
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainer trainer = new Trainer(1, new HashSet<>(), userTrainer, new TrainingType(1, "YOGA"));

		Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
		String act = trainerServiceImpl.getTrainerEmail("trainer");
		assertEquals(userTrainer.getEmail(), act);
	}

	@Test
	void getTrainerEmailException() {
		Mockito.when(trainerRepository.findByUserUserName("trainer")).thenReturn(null);
		assertThrows(TrainerException.class, () -> {
			trainerServiceImpl.getTrainerEmail("trainer");
		});
	}
}
