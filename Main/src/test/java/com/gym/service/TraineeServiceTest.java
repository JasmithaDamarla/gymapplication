package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.gym.model.Training;
import com.gym.model.TrainingType;
import com.gym.model.User;
import com.gym.repository.TraineeRepository;
import com.gym.repository.TrainerRepository;
import com.gym.repository.TrainingRepository;
import com.gym.repository.TrainingTypeRepository;
import com.gym.repository.UserRepository;
import com.gym.service.implementation.TraineeServiceImpl;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

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
	private TraineeServiceImpl traineeServiceImpl;

	@Test
	void traineeRegistration() {
		LocalDate date = LocalDate.of(2023, 8, 1);
		String generatedPassword = "76AD&^jk";
		TraineeRegistrationDTO traineeRegister = new TraineeRegistrationDTO("ben", "stone", "benStone@gmail.com", date,
				"Hyd");
		Mockito.when(passwordEncoder.encode("1234")).thenReturn(generatedPassword);
		User user = User.builder().firstName(traineeRegister.getFirstName()).lastName(traineeRegister.getLastName())
				.password(generatedPassword).email(traineeRegister.getEmail()).userName("benStone").build();
		Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
		DetailsDTO actual = traineeServiceImpl.traineeRegistration(traineeRegister);
		assertEquals(user.getUserName(), actual.getUserName());
		Mockito.verify(passwordEncoder, times(1)).encode(anyString());
	}

	@Test
	void getTraineeProfileSuccess() throws UserException, TrainingTypeException {
		String userName = "trainee";
		LocalDate dob = LocalDate.of(2000, 1, 1);

		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(),
				new User(1, "trainee", "Trainee", "User", "password", true, "trainee@gmail.com"));
		when(traineeRepository.findByUserUserName(userName)).thenReturn(trainee);

		Trainer trainer1 = new Trainer(1, new HashSet<>(),
				new User(2, "trainer1", "Trainer", "One", "password", true, "trainer1@gmail.com"),
				new TrainingType(1, "Yoga"));
		Trainer trainer2 = new Trainer(2, new HashSet<>(),
				new User(3, "trainer2", "Trainer", "Two", "password", true, "trainer2@gmail.com"),
				new TrainingType(2, "Fitness"));
		trainee.getTrainers().add(trainer1);
		trainee.getTrainers().add(trainer2);

		when(userRepository.findById(anyInt())).thenReturn(Optional.of(trainer1.getUser()));
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(trainer2.getUser()));
		when(trainingTypeRepository.findById(anyInt())).thenReturn(Optional.of(trainer1.getTrainingType()));
		when(trainingTypeRepository.findById(anyInt())).thenReturn(Optional.of(trainer2.getTrainingType()));

		TraineeProfileResponseDTO responseDTO = traineeServiceImpl.getTraineeProfile(userName);
		assertEquals(2, responseDTO.getTrainersList().size());
	}
	
	@Test
	void getTraineeProfileTraineeNotFound() {
		String userName = "trainee";
		LocalDate dob = LocalDate.of(2000, 1, 1);

		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(),
				new User(1, "trainee", "Trainee", "User", "password", true, "trainee@example.com"));
		Trainer trainer = new Trainer(1, new HashSet<>(),
				new User(2, "trainer1", "Trainer", "One", "password", true, "trainer1@example.com"),
				new TrainingType(1, "Yoga"));
		trainee.getTrainers().add(trainer);

		when(traineeRepository.findByUserUserName(userName)).thenReturn(null);

		assertThrows(TraineeException.class, () -> traineeServiceImpl.getTraineeProfile(userName));
	}


	@Test
	void getTraineeProfileTrainingTypeNotFound() {
		String userName = "trainee";
		LocalDate dob = LocalDate.of(2000, 1, 1);

		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(),
				new User(1, "trainee", "Trainee", "User", "password", true, "trainee@example.com"));
		Trainer trainer = new Trainer(1, new HashSet<>(),
				new User(2, "trainer1", "Trainer", "One", "password", true, "trainer1@example.com"),
				new TrainingType(1, "Yoga"));
		trainee.getTrainers().add(trainer);

		when(traineeRepository.findByUserUserName(userName)).thenReturn(trainee);
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(trainer.getUser()));
		when(trainingTypeRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(TrainingTypeException.class, () -> traineeServiceImpl.getTraineeProfile(userName));
	}

	@Test
	void updateTraineeProfileSucess() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		Trainer trainer = new Trainer(1, Set.of(trainee), userTrainer, new TrainingType(1, "YOGA"));
		List<Trainer> trainers = new ArrayList<>();
		trainers.add(trainer);
		trainee.setTrainers(trainers);
		trainee.setAddress("Hyd");
		Mockito.when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(userTrainee));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(userTrainee);
		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		Mockito.when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
		Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTrainer));
		Mockito.when(trainingTypeRepository.findById(anyInt())).thenReturn(Optional.of(new TrainingType(1, "YOGA")));
		TraineeProfileResponseDTO responseDTO = TraineeProfileResponseDTO.builder().userName("trainee")
				.firstName("trainee").lastName("trainee").dob(dob)
				.trainersList(List.of(new TrainerProfile("trainer", "trainer", "trainer", "YOGA"))).isActive(true)
				.address("Hyd").build();

		TraineeProfileRequestDTO request = new TraineeProfileRequestDTO("trainee", "trainee", "trainee", dob, "Hyd",
				true);
		TraineeProfileResponseDTO actual = traineeServiceImpl.updateTraineeProfile(request);
//	    assertEquals(responseDTO.getUserName(), actual.getUserName());
		assertEquals(responseDTO, actual);
		Mockito.verify(userRepository, times(1)).findByUserName(anyString());
		Mockito.verify(traineeRepository, times(1)).findByUserUserName(anyString());
	}

	@Test
	void updateTraineeProfileUserNotFound() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		TraineeProfileRequestDTO dto = new TraineeProfileRequestDTO("trainee", "trainee", "trainee", dob, "Hyd", false);
		Mockito.when(userRepository.findByUserName("trainee")).thenReturn(Optional.empty());

		assertThrows(UserException.class, () -> {
			traineeServiceImpl.updateTraineeProfile(dto);
		});

		Mockito.verify(userRepository, times(1)).findByUserName(anyString());
		Mockito.verify(traineeRepository, never()).findByUserUserName(anyString());
		Mockito.verify(traineeRepository, never()).save(any(Trainee.class));

	}

	@Test
	void updateTraineeProfileTrainingTypeException() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		Trainer trainer = new Trainer(1, Set.of(trainee), userTrainer, new TrainingType(1, "YOGA"));
		List<Trainer> trainers = new ArrayList<>();
		trainers.add(trainer);
		trainee.setTrainers(trainers);
		trainee.setAddress("Hyd");

		Mockito.when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(userTrainee));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(userTrainee);
		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		Mockito.when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
		Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(userTrainer));
		Mockito.when(trainingTypeRepository.findById(anyInt())).thenReturn(Optional.empty());
		TraineeProfileRequestDTO dto = new TraineeProfileRequestDTO("trainee", "trainee", "trainee", dob, "Hyd", false);
		assertThrows(TrainingTypeException.class, () -> {
			traineeServiceImpl.updateTraineeProfile(dto);
		});

		Mockito.verify(userRepository, times(1)).findByUserName(anyString());
		Mockito.verify(traineeRepository, times(1)).findByUserUserName(anyString());
		Mockito.verify(traineeRepository, times(1)).save(any(Trainee.class));
	}

	@Test
	void getTraineeTrainings() {
		String username = "ben";
		LocalDate periodFrom = LocalDate.of(2023, 8, 1);
		LocalDate periodTo = LocalDate.of(2023, 8, 10);
		String trainerName = "trainer1";
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
		List<Training> exp = new ArrayList<>();
		exp.add(new Training(1, trainee, trainer, "YOGA", new TrainingType(1, "YOGA"), dob, 2));
		TraineeTrainingsDTO dto = new TraineeTrainingsDTO(username, periodFrom, periodTo, trainerName, trainingType);
		Mockito.when(
				trainingRepository.findTrainingsForTrainee(username, periodFrom, periodTo, trainerName, trainingType))
				.thenReturn(exp);
		List<TraineeTrainingTypeResponseDTO> act = traineeServiceImpl.getTraineeTrainings(dto);
		assertEquals(exp.size(), act.size());
	}

	@Test
	void deleteTraineeByUserNameSuccess() throws TraineeException {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainer trainer = new Trainer(1, new HashSet<>(), userTrainer, new TrainingType(1, "YOGA"));
		List<Trainer> trainers = new ArrayList<>();
		trainers.add(trainer);
		trainee.setTrainers(trainers);

		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		Mockito.doNothing().when(trainingRepository).deleteByTraineeId(anyInt());
		for (Trainer tr : trainee.getTrainers()) {
			Mockito.when(trainerRepository.save(tr)).thenReturn(tr);
		}

		Mockito.doNothing().when(traineeRepository).deleteById(anyInt());
		Mockito.doNothing().when(userRepository).deleteByUserName(anyString());

		traineeServiceImpl.deleteByUserName("trainee");

		Mockito.verify(traineeRepository).findByUserUserName(anyString());
		Mockito.verify(trainingRepository).deleteByTraineeId(anyInt());
		Mockito.verify(traineeRepository).deleteById(anyInt());
		Mockito.verify(userRepository).deleteByUserName(anyString());
		Mockito.verify(traineeRepository).deleteById(trainee.getId());
		for (Trainer tr : trainee.getTrainers()) {
			Mockito.verify(trainerRepository).save(tr);
		}
	}

	@Test
	void deleteTraineeByUserName_TraineeNotFound() {
		Mockito.when(traineeRepository.findByUserUserName("user")).thenReturn(null);
		assertThrows(TraineeException.class, () -> {
			traineeServiceImpl.deleteByUserName("user");
		});

		Mockito.verify(traineeRepository, times(1)).findByUserUserName(anyString());
		Mockito.verifyNoMoreInteractions(trainingRepository, trainerRepository, traineeRepository, userRepository);
	}

	@Test
	void updateTrainers() {

		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		Trainer trainer = new Trainer(1, Set.of(trainee), userTrainer, new TrainingType(1, "YOGA"));
		UpdateTraineeDTO dto = new UpdateTraineeDTO("trainee", List.of("trainer"));
		List<TrainerProfile> exp = new ArrayList<>();
		exp.add(new TrainerProfile(trainer.getUser().getUserName(), trainer.getUser().getFirstName(),
				trainer.getUser().getLastName(), trainer.getTrainingType().getTrainingTypeName()));

		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
		Mockito.when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
		List<TrainerProfile> result = traineeServiceImpl.updateTrainers(dto);
		assertEquals(exp.size(), result.size());

	}
	
	@Test
	void updateTrainersTraineeNotFound() {
		UpdateTraineeDTO dto = new UpdateTraineeDTO("trainee", List.of("trainer"));
		
		Mockito.when(traineeRepository.findByUserUserName("trainee")).thenReturn(null);
		assertThrows(TraineeException.class, ()->{
			traineeServiceImpl.updateTrainers(dto);
		});
		Mockito.verify(traineeRepository,times(1)).findByUserUserName(anyString());

	}

	@Test
	void updateTrainersTrainerNotFound() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		
		UpdateTraineeDTO dto = new UpdateTraineeDTO("trainee", List.of("trainer"));
		
		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		Mockito.when(trainerRepository.findByUserUserName("trainer")).thenReturn(null);
		assertThrows(TrainerException.class, ()->{
			traineeServiceImpl.updateTrainers(dto);
		});
		Mockito.verify(traineeRepository,times(1)).findByUserUserName(anyString());
		Mockito.verify(trainerRepository,times(1)).findByUserUserName(anyString());

	}
	
	@Test
	void getTraineeEmailSucess() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		String act = traineeServiceImpl.getTraineeEmail("trainee");
		assertEquals(userTrainee.getEmail(), act);
	}

	@Test
	void getTraineeEmailException() {
		Mockito.when(traineeRepository.findByUserUserName("trainee")).thenReturn(null);
		assertThrows(TraineeException.class, () -> {
			traineeServiceImpl.getTraineeEmail("trainee");
		});
	}

	@Test
	void updateTraineeTrainers() {
	    LocalDate dob = LocalDate.of(2000, 1, 1);
	    User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
	    User userTrainer = new User(1, "trainer", "trainer", "trainer", "password", true, "trainer@example.com");
	    Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
	    Trainer trainer = new Trainer(1, new HashSet<>(), userTrainer, new TrainingType(1, "YOGA"));
	    trainee.getTrainers().add(trainer);
	    trainee.setTrainers(trainee.getTrainers());
	    List<String> list = new ArrayList<>();
	    list.add("trainer");
	    TraineeTrainersDTO dto = new TraineeTrainersDTO("trainee", list);

	    Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
	    Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
	    Mockito.when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

	    List<TrainerProfile> exp = new ArrayList<>();
		exp.add(new TrainerProfile(trainer.getUser().getUserName(), trainer.getUser().getFirstName(),
				trainer.getUser().getLastName(), trainer.getTrainingType().getTrainingTypeName()));
	    List<TrainerProfile> result = traineeServiceImpl.updateTraineesTrainers(dto);
	    assertEquals(exp.size(), result.size());
	}

	
	@Test
	void updateTraineeTrainersTraineeNotFound() {
		TraineeTrainersDTO dto = new TraineeTrainersDTO("trainee", List.of("trainer"));
		
		Mockito.when(traineeRepository.findByUserUserName("trainee")).thenReturn(null);
		assertThrows(TraineeException.class, ()->{
			traineeServiceImpl.updateTraineesTrainers(dto);
		});
		Mockito.verify(traineeRepository,times(1)).findByUserUserName(anyString());

	}

	@Test
	void updateTraineeTrainersTrainerNotFound() {
		LocalDate dob = LocalDate.of(2000, 1, 1);
		User userTrainee = new User(1, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(), userTrainee);
		
		TraineeTrainersDTO dto = new TraineeTrainersDTO("trainee", List.of("trainer"));
		
		Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
		Mockito.when(trainerRepository.findByUserUserName("trainer")).thenReturn(null);
		assertThrows(TrainerException.class, ()->{
			traineeServiceImpl.updateTraineesTrainers(dto);
		});
		Mockito.verify(traineeRepository,times(1)).findByUserUserName(anyString());
		Mockito.verify(trainerRepository,times(1)).findByUserUserName(anyString());

	}
	
//	@Test
//	void updateTraineeProfileTraineeUserNotFound() {
//	    LocalDate dob = LocalDate.of(2000, 1, 1);
//	    User userTrainee = new User(100, "trainee", "trainee", "trainee", "password", true, "trainee@example.com");
//		Trainee trainee = new Trainee(1, dob, "Address", new ArrayList<>(),userTrainee);
//	    
//		Mockito.when(userRepository.findByUserName("trainee")).thenReturn(Optional.of(userTrainee));
//		Mockito.when(userRepository.save(any(User.class))).thenReturn(userTrainee);
//	    Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(trainee);
//	    Mockito.when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
//	    Mockito.when(userRepository.findById(anyInt())).thenReturn(null);
//	    
//	    TraineeProfileRequestDTO dto = new TraineeProfileRequestDTO("trainee","trainee","trainee",dob,"Hyd",false);
//        assertThrows(UserException.class, () -> {
//            traineeServiceImpl.updateTraineeProfile(dto);
//        });
//
//        Mockito.verify(userRepository, times(1)).findByUserName(anyString());
//        Mockito.verify(traineeRepository, times(1)).findByUserUserName(anyString());
//        Mockito.verify(traineeRepository, times(1)).save(any(Trainee.class));
//        Mockito.verify(userRepository, times(1)).findById(anyInt());
//	}

}
