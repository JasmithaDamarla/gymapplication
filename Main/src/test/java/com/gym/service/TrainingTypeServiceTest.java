package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.gym.dto.TrainingTypeDTO;
import com.gym.model.TrainingType;
import com.gym.repository.TrainingTypeRepository;
import com.gym.service.implementation.TrainingTypeServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

	@InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void testGetAllTrainingTypes() {
        TrainingType trainingType = new TrainingType(1, "yoga");
        TrainingTypeDTO trainingTypeDto = new TrainingTypeDTO(1, "yoga");
        List<TrainingType> listOfTrainingTypes = new ArrayList<>(List.of(trainingType));
        List<TrainingTypeDTO> listOfTrainingTypeDtos = new ArrayList<>(List.of(trainingTypeDto));
        when(trainingTypeRepository.findAll()).thenReturn(listOfTrainingTypes);
        when(modelMapper.map(trainingType, TrainingTypeDTO.class)).thenReturn(trainingTypeDto);
        assertEquals(listOfTrainingTypeDtos.size(), trainingTypeService.getAllTrainingTypes().size());

    }

 
}
