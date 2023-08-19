package com.gym.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.EmailDTO;
import com.gym.dto.request.TrainingRequestDTO;
import com.gym.exceptions.TrainingException;
import com.gym.proxy.EmailProxy;
import com.gym.proxy.ReportProxy;
import com.gym.service.interfaces.TraineeService;
import com.gym.service.interfaces.TrainerService;
import com.gym.service.interfaces.TrainingService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/main/training")
@RestController
public class TrainingApi {

	@Autowired
	private TrainingService trainingService;
	@Autowired
	private TrainerService trainerService;
	@Autowired
	private TraineeService traineeService;
	@Autowired
	private EmailProxy emailProxy;
	@Autowired
	private ReportProxy reportProxy;

	@PostMapping("/addTraining")
	public ResponseEntity<Void> addTraining(@Valid @RequestBody TrainingRequestDTO training) throws TrainingException {
		trainingService.addTraining(training);
		log.info("training added successfully.");
		emailProxy.sendEmail(EmailDTO.builder().fromEmail("jasmithadamarla30gmail.com")
				.subject("Added trainiing")
				.ccEmail(traineeService.getTraineeEmail(training.getTraineeUserName()))
				.toEmail(trainerService.getTrainerEmail(training.getTrainerUserName())).status("NEW")
				.body("You are added to training name of " + training.getTrainingName() + " under trainer "
						+ training.getTrainerUserName())
				.remarks("Sent").build());
		
		log.info("mail sent..");
		reportProxy.addTrainingSummary(trainingService.getTrainings(training.getTrainerUserName(), training.getTrainingDate()));
		log.info("reports updated");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
