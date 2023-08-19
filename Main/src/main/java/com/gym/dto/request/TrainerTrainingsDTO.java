package com.gym.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingsDTO {

	@NotBlank(message = "UserName should not be empty")
	private String userName;
	private LocalDate periodFrom;
	private LocalDate periodTo;
	private String traineeName;
}
