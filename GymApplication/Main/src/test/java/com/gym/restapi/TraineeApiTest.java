package com.gym.restapi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import com.gym.service.kafka.EmailKafkaProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.dto.DetailsDTO;
import com.gym.dto.EmailDTO;
import com.gym.dto.request.TraineeProfileRequestDTO;
import com.gym.dto.request.TraineeRegistrationDTO;
import com.gym.dto.request.TraineeTrainersDTO;
import com.gym.dto.request.TraineeTrainingsDTO;
import com.gym.dto.request.UpdateTraineeDTO;
import com.gym.dto.response.TraineeProfileResponseDTO;
import com.gym.dto.response.TraineeTrainingTypeResponseDTO;
import com.gym.dto.response.TrainerProfile;
import com.gym.exceptions.TraineeException;
import com.gym.exceptions.UserException;
import com.gym.proxy.EmailProxy;
import com.gym.service.interfaces.TraineeService;

@WebMvcTest(TraineeApi.class)
class TraineeApiTest {

	@MockBean
	private TraineeService traineeService;
	@MockBean
	private EmailKafkaProducer emailProxy;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void registerTraineeSuccess() throws JsonProcessingException, Exception {
		TraineeRegistrationDTO traineeRegister = new TraineeRegistrationDTO("ben", "stone", "benStone@gmail.com", null,
				"Hyd");
		DetailsDTO dto = new DetailsDTO("ben", "56f#%&&");
		Mockito.when(traineeService.traineeRegistration(any(TraineeRegistrationDTO.class))).thenReturn(dto);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(post("/main/trainee/traineeRegistration").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(traineeRegister))).andExpect(status().isCreated());
	}

	@Test
	void deleteTrainee() throws Exception {
		String userName = "user";
		Mockito.doNothing().when(traineeService).deleteByUserName(userName);
		mockMvc.perform(delete("/main/trainee/deleteByUserName").param("username", userName))
				.andExpect(status().isOk());
		Mockito.verify(traineeService, Mockito.times(1)).deleteByUserName(userName);
	}

	@Test
	void viewTraineeProfile() throws Exception {
		TraineeProfileResponseDTO trainee = TraineeProfileResponseDTO.builder().userName("ben").firstName("ben")
				.lastName("stone").trainersList(List.of(new TrainerProfile())).isActive(true).address("Hyd").build();
		Mockito.when(traineeService.getTraineeProfile(anyString())).thenReturn(trainee);
		mockMvc.perform(get("/main/trainee/profile").param("userName", "ben")).andExpect(status().isOk());

	}

	@Test
	void updateTraineeProfile() throws Exception {
		TraineeProfileResponseDTO responseDTO = TraineeProfileResponseDTO.builder().userName("ben").firstName("ben")
				.lastName("stone").trainersList(List.of(new TrainerProfile())).isActive(true).address("Hyd").build();

		Mockito.when(traineeService.updateTraineeProfile(any(TraineeProfileRequestDTO.class))).thenReturn(responseDTO);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(put("/main/trainee/profile").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(responseDTO))).andExpect(status().isOk());
	}

	@Test
	void updateTrainers() throws Exception {
		List<TrainerProfile> trainersList = List.of(TrainerProfile.builder().firstName("ben").lastName("stone")
				.userName("benstone").specialization("YOGA").build());
		Mockito.when(traineeService.updateTrainers(any(UpdateTraineeDTO.class))).thenReturn(trainersList);
		mockMvc.perform(put("/main/trainee/updateTrainers").contentType(MediaType.APPLICATION_JSON).content(
				new ObjectMapper().writeValueAsString(new UpdateTraineeDTO("ben", List.of("trainer1", "trainer2")))))
				.andExpect(status().isOk());
	}

	@Test
	void getTrainees() throws Exception {
		String username = "ben";
		LocalDate periodFrom = LocalDate.of(2023, 8, 1);
		LocalDate periodTo = LocalDate.of(2023, 8, 10);
		String trainerName = "trainer1";
		String trainingType = "YOGA";

		Mockito.when(traineeService.getTraineeTrainings(any(TraineeTrainingsDTO.class)))
				.thenReturn(List.of(TraineeTrainingTypeResponseDTO.builder().build()));

		mockMvc.perform(get("/main/trainee/getTrainees").param("username", username)
				.param("periodFrom", periodFrom.toString()).param("periodTo", periodTo.toString())
				.param("trainerName", trainerName).param("trainingType", trainingType)).andExpect(status().isOk());
	}

	@Test
	void updateTraineeTrainers() throws Exception {
		List<TrainerProfile> trainersList = List.of(TrainerProfile.builder().firstName("ben").lastName("stone")
				.userName("benstone").specialization("YOGA").build());
		Mockito.when(traineeService.updateTraineesTrainers(any(TraineeTrainersDTO.class))).thenReturn(trainersList);
		mockMvc.perform(put("/main/trainee/traineeTrainers").contentType(MediaType.APPLICATION_JSON).content(
				new ObjectMapper().writeValueAsString(new TraineeTrainersDTO("ben", List.of("trainer1", "trainer2")))))
				.andExpect(status().isOk());
	}

	/*@Test
	void getTraineesException() throws Exception {
		String username = "ben";
		LocalDate periodFrom = LocalDate.of(2023, 8, 1);
		LocalDate periodTo = LocalDate.of(2023, 8, 10);
		String trainerName = "trainer1";
		String trainingType = "YOGA";

		Mockito.when(traineeService.getTraineeTrainings(any(TraineeTrainingsDTO.class)))
				.thenThrow(TraineeException.class);

		mockMvc.perform(get("/main/trainee/getTrainees").param("username", username)
				.param("periodFrom", periodFrom.toString()).param("periodTo", periodTo.toString())
				.param("trainerName", trainerName).param("trainingType", trainingType))
				.andExpect(status().isBadRequest());
	}

	@Test
	void viewTraineeProfileException() throws Exception {

		Mockito.when(traineeService.getTraineeProfile(anyString())).thenThrow(UserException.class);
		mockMvc.perform(get("/main/trainee/profile").param("userName", "ben")).andExpect(status().isBadRequest());

	}
	
	@Test
	void registerTraineeException() throws JsonProcessingException, Exception {
		TraineeRegistrationDTO traineeRegister = new TraineeRegistrationDTO("ben", "stone", "benStone@gmail.com", null,
				"Hyd");
		Mockito.when(traineeService.traineeRegistration(any(TraineeRegistrationDTO.class))).thenThrow(DataIntegrityViolationException.class);
		Mockito.doNothing().when(emailProxy).sendEmail(EmailDTO.builder().build());
		mockMvc.perform(post("/main/trainee/traineeRegistration").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(traineeRegister))).andExpect(status().isBadRequest());
	}*/

}
