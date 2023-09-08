package com.gym.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainersDTO {
	
	@NotBlank(message ="trainee user name should not be empty")
	private String traineeUserName;
	@NotNull(message = "add atleast one trainer")
	private List<String> trainerUserNames;

}
