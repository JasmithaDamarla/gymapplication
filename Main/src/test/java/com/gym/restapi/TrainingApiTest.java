package com.gym.restapi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gym.dto.EmailDTO;
import com.gym.dto.TrainingsSummaryDTO;
import com.gym.dto.request.TrainingRequestDTO;
import com.gym.proxy.EmailProxy;
import com.gym.proxy.ReportProxy;
import com.gym.service.interfaces.TraineeService;
import com.gym.service.interfaces.TrainerService;
import com.gym.service.interfaces.TrainingService;

@WebMvcTest(TrainingApi.class)
class TrainingApiTest {
	
	@MockBean
	private TrainingService trainingService;
	@MockBean
	private TrainerService trainerService;
	@MockBean
	private TraineeService traineeService;
	@MockBean
	private EmailProxy emailProxy;
	@MockBean
	private ReportProxy reportProxy;
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void addTrainingSucess() throws JsonProcessingException, Exception {
	    LocalDate date = LocalDate.of(2023, 8, 1);
	    TrainingRequestDTO request = new TrainingRequestDTO("trainee","trainer","Batch1",date,1);
	    
	    Mockito.doNothing().when(trainingService).addTraining(request);
	    Mockito.doNothing().when(emailProxy).sendEmail(any(EmailDTO.class));
	    TrainingsSummaryDTO summary = TrainingsSummaryDTO.builder()
	            .trainerFirstName("trainer")
	            .date(date)
	            .trainerLastName("Stone")
	            .trainerStatus("ACTIVE")
	            .trainerUserName("trainer")
	            .build();
	    Mockito.when(trainingService.getTrainings("trainer", date)).thenReturn(summary);
	    
	    Mockito.when(reportProxy.addTrainingSummary(any(TrainingsSummaryDTO.class))).thenReturn(null);

	    
	    
	    mockMvc.perform(post("/main/training/addTraining")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
	            .andExpect(status().isCreated());

	    Mockito.verify(trainingService, times(1)).addTraining(any(TrainingRequestDTO.class));
	    Mockito.verify(emailProxy, times(1)).sendEmail(any(EmailDTO.class));
	    Mockito.verify(reportProxy, times(1)).addTrainingSummary(any(TrainingsSummaryDTO.class));
	       
	}
}
