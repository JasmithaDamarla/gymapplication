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
public class UpdateTraineeDTO {
	
	@NotBlank(message = "UserName should not be empty")
	private String userName;
	@NotNull(message = "add atleast one trainer")
	private List<String> trainers;
	
}
