package com.gym.service.interfaces;

import java.util.List;

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

public interface TraineeService {
	DetailsDTO traineeRegistration(TraineeRegistrationDTO traineeRegistrationDTO);
	TraineeProfileResponseDTO getTraineeProfile(String userName) throws UserException,TrainingTypeException;
	void deleteByUserName(String userName);
	TraineeProfileResponseDTO updateTraineeProfile(TraineeProfileRequestDTO profileDTO) throws UserException,TrainingTypeException;
	List<TrainerProfile> updateTrainers(UpdateTraineeDTO trainee) throws UserException,TrainerException;
	List<TraineeTrainingTypeResponseDTO> getTraineeTrainings(TraineeTrainingsDTO traineeTraining);
	List<TrainerProfile> updateTraineesTrainers(TraineeTrainersDTO traineeTrainersDTO);
	String getTraineeEmail(String userName) throws TraineeException;
	
}
