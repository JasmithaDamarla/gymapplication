package com.gym.service.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.dto.TrainingTypeDTO;
import com.gym.repository.TrainingTypeRepository;
import com.gym.service.interfaces.TrainingTypeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

	@Autowired
	private TrainingTypeRepository trainingTypeRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<TrainingTypeDTO> getAllTrainingTypes() {
		log.info("TrainingTypeServiceImpl : GetAllTrainingTypes ");
		return trainingTypeRepository.findAll().stream()
				.map(trainingType -> modelMapper.map(trainingType, TrainingTypeDTO.class)).toList();

	}

}
