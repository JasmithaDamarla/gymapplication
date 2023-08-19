package com.gym.restapi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.dto.DetailsDTO;
import com.gym.dto.EmailDTO;
import com.gym.dto.request.TrainerRegistrationDTO;
import com.gym.dto.request.TrainerTrainingsDTO;
import com.gym.dto.request.UpdateTrainerProfileDTO;
import com.gym.dto.response.TraineeDetails;
import com.gym.dto.response.TrainerProfile;
import com.gym.dto.response.TrainerProfileResponseDTO;
import com.gym.dto.response.TrainerTrainingTypeResponseDTO;
import com.gym.exceptions.TrainerException;
import com.gym.proxy.EmailProxy;
import com.gym.service.interfaces.TrainerService;

@WebMvcTest(TrainerApi.class)
class TrainerApiTest {

	@MockBean
	private TrainerService trainerService;
	@MockBean
	private EmailProxy emailProxy;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void registerTraineeSuccess() throws JsonProcessingException, Exception {
		TrainerRegistrationDTO trainerRegister = new TrainerRegistrationDTO("ben", "stone", "benStone@gmail.com",
				"YOGA");
		DetailsDTO dto = new DetailsDTO("ben", "56f#%&&");
		Mockito.when(trainerService.trainerRegistration(any(TrainerRegistrationDTO.class))).thenReturn(dto);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(post("/main/trainer/trainerRegistration").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainerRegister))).andExpect(status().isCreated());
	}

	@Test
	void getTrainerProfile() throws Exception {
		TrainerProfileResponseDTO trainer = TrainerProfileResponseDTO.builder().username("ben").firstName("ben")
				.lastName("stone").trainees(List.of(new TraineeDetails())).specialization("YOGA").isactive(true)
				.build();
		Mockito.when(trainerService.getTrainerProfile(anyString())).thenReturn(trainer);
		mockMvc.perform(get("/main/trainer/getTrainerProfile").param("userName", "ben")).andExpect(status().isOk());
	}

	@Test
	void updateTrainerProfile() throws Exception {
		TrainerProfileResponseDTO responseDTO = TrainerProfileResponseDTO.builder().username("ben").firstName("ben")
				.lastName("stone").trainees(List.of(new TraineeDetails())).specialization("YOGA").isactive(true)
				.build();
		UpdateTrainerProfileDTO request = new UpdateTrainerProfileDTO("ben", "ben", "stone", "YOGA", false);
		Mockito.when(trainerService.updateTrainerProfile(request)).thenReturn(responseDTO);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(put("/main/trainer/updateTrainerProfile").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isOk());
	}

	@Test
	void getNotAssignedTrainers() throws Exception {
		List<TrainerProfile> trainers = List.of(TrainerProfile.builder().build());
		Mockito.when(trainerService.getNotAssignedTrainees(anyString())).thenReturn(trainers);
		mockMvc.perform(get("/main/trainer/notAssignedTrainees").param("userName", "ben")).andExpect(status().isOk());
	}

	@Test
	void getTrainerTrainings() throws Exception {

		String username = "ben";
		LocalDate periodFrom = LocalDate.of(2023, 8, 1);
		LocalDate periodTo = LocalDate.of(2023, 8, 10);
		String trainerName = "trainer1";
		String trainingType = "YOGA";

		Mockito.when(trainerService.getTrainerTrainings(any(TrainerTrainingsDTO.class)))
				.thenReturn(List.of(TrainerTrainingTypeResponseDTO.builder().build()));

		mockMvc.perform(get("/main/trainer/getTrainersTrainings").param("userName", username)
				.param("periodFrom", periodFrom.toString()).param("periodTo", periodTo.toString())
				.param("trainerName", trainerName).param("trainingType", trainingType)).andExpect(status().isOk());
	}
	
	/*@Test
	void registerTraineeFail() throws JsonProcessingException, Exception {
		TrainerRegistrationDTO trainerRegister = new TrainerRegistrationDTO("ben", "stone", "benStone@gmail.com",
				"YOGA");
		Mockito.when(trainerService.trainerRegistration(any(TrainerRegistrationDTO.class))).thenThrow(TrainerException.class);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(post("/main/trainer/trainerRegistration").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainerRegister))).andExpect(status().isBadRequest());
	}
	
	@Test
	void updateTrainerProfileFail() throws Exception {

		UpdateTrainerProfileDTO request = new UpdateTrainerProfileDTO("ben", "ben", "stone", "YOGA", false);
		Mockito.when(trainerService.updateTrainerProfile(request)).thenThrow(RuntimeException.class);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(put("/main/trainer/updateTrainerProfile").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isInternalServerError());
	}

	@Test
	void registerTraineeValidationFail() throws JsonProcessingException, Exception {
		TrainerRegistrationDTO trainerRegister = new TrainerRegistrationDTO("ben", "stone", "benStonegmail.com",
				"YOGA");
		Mockito.when(trainerService.trainerRegistration(any(TrainerRegistrationDTO.class))).thenThrow(MethodArgumentNotValidException.class);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(post("/main/trainer/trainerRegistration").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainerRegister))).andExpect(status().isBadRequest());
	}*/
}
