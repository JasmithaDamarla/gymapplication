package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.gym.dto.TrainingsSummaryDTO;
import com.gym.model.TrainingsSummary;
import com.gym.repository.TrainingsSummaryRepository;

@ExtendWith(MockitoExtension.class)
class TrainingsSummaryServiceTest {

	@Mock(lenient = true)
	private ModelMapper modelMapper;
	@Mock(lenient = true)
	private TrainingsSummaryRepository trainingsSummaryRepository;
	@InjectMocks
	private TrainingsSummaryServiceImpl trainingsSummaryServiceImpl;

	@Test
	void getTrainingsSummary() {
		List<TrainingsSummary> summaryList = new ArrayList<>();
		summaryList.add(new TrainingsSummary());
		Mockito.when(trainingsSummaryRepository.findAll()).thenReturn(summaryList);
		List<TrainingsSummary> result = trainingsSummaryServiceImpl.getTrainingsSummary();
		assertEquals(summaryList, result);
	}

	@Test
	void addAllTrainingsSummary() {
		List<TrainingsSummaryDTO> summaryList = new ArrayList<>();
        summaryList.add(new TrainingsSummaryDTO("userName1", "firstName1", "lastName1", "ACTIVE",null));
        summaryList.add(new TrainingsSummaryDTO("userName2", "firstName2", "lastName2", "INACTIVE",null));

        TrainingsSummary trainingsSummary1 = new TrainingsSummary("userName1", "firstName1", "lastName1", "ACTIVE",null);
        TrainingsSummary trainingsSummary2 = new TrainingsSummary("userName2", "firstName2", "lastName2", "INACTIVE",null);
        Mockito.when(modelMapper.map(any(TrainingsSummaryDTO.class), any())).thenReturn(trainingsSummary1, trainingsSummary2);
        trainingsSummaryServiceImpl.addAllTrainingsSummary(summaryList);
		Mockito.verify(trainingsSummaryRepository, times(summaryList.size())).save(any(TrainingsSummary.class));
	}

	@Test
	void addTrainingsSummary() {
		TrainingsSummaryDTO trainingSummaryDTO = new TrainingsSummaryDTO("userName", "firstName", "lastName", "ACTIVE",
				null);
		TrainingsSummary trainingSummary = new TrainingsSummary("userName", "firstName", "lastName", "ACTIVE", null);
		Mockito.when(modelMapper.map(trainingSummaryDTO, TrainingsSummary.class)).thenReturn(trainingSummary);
		trainingsSummaryServiceImpl.addTrainingsSummary(trainingSummaryDTO);
		Mockito.verify(trainingsSummaryRepository, times(1)).save(trainingSummary);
	}
}
