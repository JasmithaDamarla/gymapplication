package com.gym.service.interfaces;

import java.util.List;

import com.gym.dto.DetailsDTO;
import com.gym.dto.request.TrainerRegistrationDTO;
import com.gym.dto.request.TrainerTrainingsDTO;
import com.gym.dto.request.UpdateTrainerProfileDTO;
import com.gym.dto.response.TrainerProfile;
import com.gym.dto.response.TrainerProfileResponseDTO;
import com.gym.dto.response.TrainerTrainingTypeResponseDTO;
import com.gym.exceptions.TrainerException;
import com.gym.exceptions.UserException;

public interface TrainerService {
	DetailsDTO trainerRegistration(TrainerRegistrationDTO trainerRegistrationDTO);
	TrainerProfileResponseDTO getTrainerProfile(String userName);
	TrainerProfileResponseDTO updateTrainerProfile(UpdateTrainerProfileDTO profileDTO);
	List<TrainerProfile> getNotAssignedTrainees(String userName);
	List<TrainerTrainingTypeResponseDTO> getTrainerTrainings(TrainerTrainingsDTO traineeTraining)
			throws UserException, TrainerException;
	String getTrainerEmail(String userName) throws TrainerException;
	
}
