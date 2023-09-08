package com.gym.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingTypeResponseDTO {
	
	private String trainingName;
	private LocalDate trainingDate;
	private String trainingType;
	private int duration;
	private String trainerName;
}
