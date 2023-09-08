package com.gym.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequestDTO {

	@NotBlank(message = "traineeUserName should not be empty")
	private String traineeUserName;
	@NotBlank(message = "trainerUserName should not be empty")
	private String trainerUserName;
	@NotBlank(message = "trainingName should not be empty")
	private String trainingName;
	@DateTimeFormat(pattern = "DD-MM-YYYY")
	@NotNull
	private LocalDate trainingDate;
//	@NotBlank(message = "trainingType should not be empty")
//	private String trainingType;
	@Min(1)
	private int traningDuration;
}
