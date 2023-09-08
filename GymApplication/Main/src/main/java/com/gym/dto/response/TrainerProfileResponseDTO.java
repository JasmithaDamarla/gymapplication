package com.gym.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileResponseDTO {

	private String username;
	private String firstName;
	private String lastName;
	private String specialization;
	private boolean isactive;
	private List<TraineeDetails> trainees;
}
