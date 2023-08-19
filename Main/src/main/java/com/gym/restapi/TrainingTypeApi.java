package com.gym.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.TrainingTypeDTO;
import com.gym.service.interfaces.TrainingTypeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/main/trainingType")
@RestController
public class TrainingTypeApi {
	
	@Autowired
	private TrainingTypeService trainingTypeService;
	
	@GetMapping
    public List<TrainingTypeDTO> getAllTrainingType() {
        log.info("TrainingTypeController : GetAllTrainingType");
        return trainingTypeService.getAllTrainingTypes();

    }

}
