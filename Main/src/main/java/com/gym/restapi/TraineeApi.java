package com.gym.restapi;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.gym.proxy.EmailProxy;
import com.gym.service.interfaces.TraineeService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/main/trainee")
@RestController
public class TraineeApi {

	@Autowired
	private TraineeService traineeService;
	@Autowired
	private EmailProxy emailProxy;
	private static final String FROM_EMAIL = "jasmithadamarla@gmail.com";

	@PostMapping("/traineeRegistration")
	public ResponseEntity<DetailsDTO> traineeRegistration(
			@Valid @RequestBody TraineeRegistrationDTO traineeRegistrationDTO) throws TraineeException {
		DetailsDTO dto = traineeService.traineeRegistration(traineeRegistrationDTO);
		log.info("trainee registered successfully.");
		String body = "UserName : " + dto.getUserName() + "\nPassword : " + dto.getPassword();
		emailProxy.sendEmail(EmailDTO.builder().fromEmail(FROM_EMAIL).ccEmail(FROM_EMAIL).subject("Trainee Registration sucessful")
				.toEmail(traineeRegistrationDTO.getEmail()).status("NEW").body(body).remarks("Sent").build());
		log.info("mail sent successful");
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@DeleteMapping("/deleteByUserName")
	public ResponseEntity<Void> deleteByUserName(@RequestParam String username) {
		log.info("deleteing user of username {}", username);
		traineeService.deleteByUserName(username);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<TraineeProfileResponseDTO> getTraineeProfile(@RequestParam String userName) {
		log.info("Fetching details of trainee of username {}", userName);
		return new ResponseEntity<>(traineeService.getTraineeProfile(userName), HttpStatus.OK);
	}

	@PutMapping("/profile")
	public ResponseEntity<TraineeProfileResponseDTO> updateTraineeProfile(
			@Valid @RequestBody TraineeProfileRequestDTO profileDTO) {
		log.info("Updating details of trainee of username {}", profileDTO.getUserName());
		TraineeProfileResponseDTO dto = traineeService.updateTraineeProfile(profileDTO);
		emailProxy.sendEmail(EmailDTO.builder()
				.ccEmail(FROM_EMAIL)
				.fromEmail(FROM_EMAIL)
				.subject("Updated Trainee")
				.toEmail(traineeService.getTraineeEmail(profileDTO.getUserName()))
				.status("ACTIVE")
				.remarks("updated successfully")
				.body("Your profile has been updated successfully")
				.build());
		log.info("mail sent..");
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PutMapping("/updateTrainers")
	public ResponseEntity<List<TrainerProfile>> updateTrainers(@Valid @RequestBody UpdateTraineeDTO trainee) {
		log.info("Fetching details of trainee of username {}", trainee.getUserName());
		return new ResponseEntity<>(traineeService.updateTrainers(trainee), HttpStatus.OK);
	}

	@GetMapping("/getTrainees")
	public ResponseEntity<List<TraineeTrainingTypeResponseDTO>> getTrainings(@RequestParam("username") String username,
			@RequestParam(value = "periodFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
			@RequestParam(value = "periodTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
			@RequestParam(value = "trainerName", required = false) String trainerName,
			@RequestParam(value = "trainingType", required = false) String trainingType) {

		TraineeTrainingsDTO trainee = TraineeTrainingsDTO.builder().trainerName(trainerName).trainingType(trainingType)
				.periodFrom(periodFrom).userName(username).periodTo(periodTo).build();
		log.info("Fetching training details of trainee of username {}", trainee.getUserName());
		return new ResponseEntity<>(traineeService.getTraineeTrainings(trainee), HttpStatus.OK);
	}

	@PutMapping("/traineeTrainers")
	public ResponseEntity<List<TrainerProfile>> updateTraineeTrainers(
			@Valid @RequestBody TraineeTrainersDTO traineeTrainers) {
		log.info("Updating details of trainee of username - {}, and trainer of username {}",
				traineeTrainers.getTraineeUserName(), traineeTrainers.getTrainerUserNames());
		return new ResponseEntity<>(traineeService.updateTraineesTrainers(traineeTrainers), HttpStatus.OK);
	}

}
