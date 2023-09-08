package com.gym.restapi;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.gym.dto.TrainingTypeDTO;
import com.gym.service.interfaces.TrainingTypeService;

@WebMvcTest(TrainingTypeApi.class)
class TrainingTypeApiTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TrainingTypeService trainingTypeService;

	@Test
	void testGetAllTrainingType() throws Exception {
		List<TrainingTypeDTO> listOfTrainingTypeDto = new ArrayList<>();
		when(trainingTypeService.getAllTrainingTypes()).thenReturn(listOfTrainingTypeDto);
		mockMvc.perform(get("/main/trainingType").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

}
