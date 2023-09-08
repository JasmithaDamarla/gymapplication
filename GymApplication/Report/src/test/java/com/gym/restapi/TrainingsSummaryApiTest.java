package com.gym.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.dto.TrainingsSummaryDTO;
import com.gym.model.TrainingsSummary;
import com.gym.service.TrainingsSummaryService;

@WebMvcTest(TrainingsSummaryApi.class)
class TrainingsSummaryApiTest {

	@MockBean
	private TrainingsSummaryService trainingsSummaryService;
	@Autowired
	private MockMvc mockMvc;
	@InjectMocks
	private TrainingsSummaryApi trainingsSummaryApi;

	@Test
	void testAddAllTrainingSummary_Success() throws Exception {
		List<TrainingsSummaryDTO> trainingsList = new ArrayList<>();
		trainingsList.add(new TrainingsSummaryDTO());
		trainingsList.add(new TrainingsSummaryDTO());

		mockMvc.perform(post("/reports/allTrainingsSummary").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainingsList))).andExpect(status().isOk());

		Mockito.verify(trainingsSummaryService).addAllTrainingsSummary(trainingsList);
	}

	@Test
	void testAddTrainingSummary_Success() throws Exception {
		TrainingsSummaryDTO trainingSummaryDTO = new TrainingsSummaryDTO();

		mockMvc.perform(post("/reports/trainingsSummary").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainingSummaryDTO))).andExpect(status().isOk());

		Mockito.verify(trainingsSummaryService).addTrainingsSummary(trainingSummaryDTO);
	}

	@Test
	void testGetTrainingSummary_Success() throws Exception {
		List<TrainingsSummary> summaryList = new ArrayList<>();
		Mockito.when(trainingsSummaryService.getTrainingsSummary()).thenReturn(summaryList);
		mockMvc.perform(get("/reports/trainingsSummary")).andExpect(status().isOk())
				.andExpect(content().contentType("text/csv")).andExpect(header().string("Content-Disposition",
						"attachment; filename=\"Trainers_Trainings_summary_YYYY_MM.csv\""));

	}
}
