package com.gym.restapi;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.DetailsDTO;
import com.gym.dto.EmailDTO;
import com.gym.dto.request.TrainerRegistrationDTO;
import com.gym.dto.request.TrainerTrainingsDTO;
import com.gym.dto.request.UpdateTrainerProfileDTO;
import com.gym.dto.response.TrainerProfile;
import com.gym.dto.response.TrainerProfileResponseDTO;
import com.gym.dto.response.TrainerTrainingTypeResponseDTO;
import com.gym.exceptions.TrainerException;
import com.gym.proxy.EmailProxy;
import com.gym.service.interfaces.TrainerService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/main/trainer")
@RestController
public class TrainerApi {

	@Autowired
	private TrainerService trainerService;
	@Autowired
	private EmailProxy emailProxy;
	private static final String FROM_EMAIL = "jasmithadamarla@gmail.com";

	@PostMapping("/trainerRegistration")
	public ResponseEntity<DetailsDTO> trainerRegistration(
			@Valid @RequestBody TrainerRegistrationDTO trainerRegistrationDTO) throws TrainerException {
		log.info("registering trainer..");
		DetailsDTO dto = trainerService.trainerRegistration(trainerRegistrationDTO);
		String body = "UserName : " + dto.getUserName() + "\nPassword : " + dto.getPassword();
		emailProxy.sendEmail(EmailDTO.builder()
									.ccEmail(FROM_EMAIL)
									.fromEmail(FROM_EMAIL)
									.subject("Trainer Registration")
									.toEmail(trainerRegistrationDTO.getEmail())
									.status("NEW")
									.remarks("registered successfully")
									.body(body)
									.build());
		log.info("mail sent..");
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@GetMapping("/getTrainerProfile")
	public ResponseEntity<TrainerProfileResponseDTO> getTrainerProfile(@RequestParam String userName)
			throws TrainerException {
		TrainerProfileResponseDTO dto = trainerService.getTrainerProfile(userName);
		log.info("trainer profile retrieved successfully.");
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PutMapping("/updateTrainerProfile")
	public ResponseEntity<TrainerProfileResponseDTO> updateTrainerProfile(
			@Valid @RequestBody UpdateTrainerProfileDTO profileDTO) throws TrainerException {
		log.info("trainer profile getting updated.");
		TrainerProfileResponseDTO dto = trainerService.updateTrainerProfile(profileDTO);
		emailProxy.sendEmail(EmailDTO.builder()
				.ccEmail(FROM_EMAIL)
				.fromEmail(FROM_EMAIL)
				.subject("Updated Trainer")
				.toEmail(trainerService.getTrainerEmail(profileDTO.getUserName()))
				.status("ACTIVE")
				.remarks("updated successfully")
				.body("Your profile has been updated successfully")
				.build());
		log.info("mail sent..");
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping("/notAssignedTrainees")
	public ResponseEntity<List<TrainerProfile>> getNotAssignedTrainees(@RequestParam String userName) {
		List<TrainerProfile> dto = trainerService.getNotAssignedTrainees(userName);
		log.info("trainees not assigned are retrieved successfully.");
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping("/getTrainersTrainings")
	public ResponseEntity<List<TrainerTrainingTypeResponseDTO>> getTrainerTrainings(@RequestParam("userName") String userName,
			@RequestParam(value = "periodFrom", required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
			@RequestParam(value = "periodTo", required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
			@RequestParam(value = "traineeName", required = false) String traineeName) {
		List<TrainerTrainingTypeResponseDTO> dto = trainerService.getTrainerTrainings(TrainerTrainingsDTO.builder()
				.userName(userName).periodFrom(periodFrom).periodTo(periodTo).traineeName(traineeName).build());
		log.info("trainers trainings data is retrieved successfully.");
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

}
