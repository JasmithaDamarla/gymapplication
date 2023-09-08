package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gym.dto.TrainingsSummaryDTO;
import com.gym.dto.request.TrainingRequestDTO;
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
import com.gym.service.implementation.TrainingServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

	@Mock
	private TrainingRepository trainingRepository;
	@Mock
	private TraineeRepository traineeRepository;
	@Mock
	private TrainerRepository trainerRepository;
	@InjectMocks
	private TrainingServiceImpl trainingServiceImpl;
	
	@Test
	void addTrainingSuccess() {
	    LocalDate date = LocalDate.of(2023, 8, 1);
	    TrainingRequestDTO dto = new TrainingRequestDTO("trainee", "trainer", "Batch", date, 2);
	    Trainee trainee = new Trainee(1, date, "Hyd", new ArrayList<>(),
	            new User(1, "trainee", "trainee", "trainee", "ben^78a", false, "trainee@gmail.com"));
	    Trainer trainer = new Trainer(1, new HashSet<>(),
	            new User(1, "trainer", "trainer", "trainer", "ben^78a", false, "trainer@gmail.com"),
	            new TrainingType(1,"YOGA"));
	    Mockito.when(traineeRepository.findByUserUserName(dto.getTraineeUserName())).thenReturn(trainee);
	    Mockito.when(trainerRepository.findByUserUserName(dto.getTrainerUserName())).thenReturn(trainer);
	    Mockito.when(trainerRepository.save(any(Trainer.class))).thenReturn(new Trainer());
	    Mockito.when(trainingRepository.save(any(Training.class))).thenReturn(new Training());
	    trainingServiceImpl.addTraining(dto);
	    Mockito.verify(traineeRepository, times(1)).findByUserUserName("trainee");
	    Mockito.verify(trainerRepository, times(1)).findByUserUserName("trainer");
	    Mockito.verify(trainerRepository, times(1)).save(trainer);
	    Mockito.verify(trainingRepository, times(1)).save(any(Training.class));
	}

	@Test
	void addTrainingTraineeNotFound() {
	    LocalDate date = LocalDate.of(2023, 8, 1);
	    TrainingRequestDTO dto = new TrainingRequestDTO("trainee", "trainer", "Batch", date, 2);
	    Mockito.when(traineeRepository.findByUserUserName(anyString())).thenReturn(null);
	    assertThrows(TraineeException.class,()->{
	    	trainingServiceImpl.addTraining(dto);
	    });
	    Mockito.verify(traineeRepository, times(1)).findByUserUserName("trainee");
	    Mockito.verify(trainerRepository, never()).findByUserUserName("trainer");
	    Mockito.verify(trainerRepository, never()).save(any(Trainer.class));
	    Mockito.verify(trainingRepository, never()).save(any(Training.class));
	}
	
	@Test
	void addTrainingTrainerNotFound() {
	    LocalDate date = LocalDate.of(2023, 8, 1);
	    TrainingRequestDTO dto = new TrainingRequestDTO("trainee", "trainer", "Batch", date, 2);
	    Trainee trainee = new Trainee(1, date, "Hyd", new ArrayList<>(),
	            new User(1, "trainee", "trainee", "trainee", "ben^78a", false, "trainee@gmail.com"));
	    Mockito.when(traineeRepository.findByUserUserName(dto.getTraineeUserName())).thenReturn(trainee);
	    Mockito.when(trainerRepository.findByUserUserName(dto.getTrainerUserName())).thenReturn(null);
	    
	    assertThrows(TrainerException.class,()->{
	    	trainingServiceImpl.addTraining(dto);
	    });
	    Mockito.verify(traineeRepository, times(1)).findByUserUserName("trainee");
	    Mockito.verify(trainerRepository, times(1)).findByUserUserName("trainer");
	    Mockito.verify(trainerRepository, never()).save(any(Trainer.class));
	    Mockito.verify(trainingRepository, never()).save(any(Training.class));
	}
	
	@Test
	void getTrainingsSuccess1() {
		LocalDate date = LocalDate.of(2023, 8, 1);
	    Trainer trainer = new Trainer(1, new HashSet<>(),
	            new User(1, "trainer", "trainer", "trainer", "ben^78a", false, "trainer@gmail.com"),
	            new TrainingType(1,"YOGA"));
	    TrainingsSummaryDTO summary = new TrainingsSummaryDTO("trainer","trainer","trainer","INACTIVE",date);
	    Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
	    TrainingsSummaryDTO actual = trainingServiceImpl.getTrainings("trainer", date);
	    assertEquals(summary,actual);
	    Mockito.verify(trainerRepository, times(1)).findByUserUserName("trainer");
	}
	
	@Test
	void getTrainingsSuccess2() {
		LocalDate date = LocalDate.of(2023, 8, 1);
	    Trainer trainer = new Trainer(1, new HashSet<>(),
	            new User(1, "trainer", "trainer", "trainer", "ben^78a", true, "trainer@gmail.com"),
	            new TrainingType(1,"YOGA"));
	    TrainingsSummaryDTO summary = new TrainingsSummaryDTO("trainer","trainer","trainer","ACTIVE",date);
	    Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(trainer);
	    TrainingsSummaryDTO actual = trainingServiceImpl.getTrainings("trainer", date);
	    assertEquals(summary,actual);
	    Mockito.verify(trainerRepository, times(1)).findByUserUserName("trainer");
	}
	
	@Test
	void getTrainingsTrainerNotFound() {
		LocalDate date = LocalDate.of(2023, 8, 1);
	    Mockito.when(trainerRepository.findByUserUserName(anyString())).thenReturn(null);
	    assertThrows(TrainerException.class,()->{
	    	trainingServiceImpl.getTrainings("trainer", date);
	    });
	    Mockito.verify(trainerRepository, times(1)).findByUserUserName("trainer");
	}
}
