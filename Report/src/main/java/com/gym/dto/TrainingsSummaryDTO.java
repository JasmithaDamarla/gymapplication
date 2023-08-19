package com.gym.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingsSummaryDTO {
	
	@NotBlank(message = "trainer user name is required")
	private String trainerUserName;
	@NotBlank(message = "trainer first name is required")
	private String trainerFirstName;
	@NotBlank(message = "trainer last name is required")
	private String trainerLastName;
	@NotBlank(message = "trainer status is required")
	private String trainerStatus;
	private LocalDate date;
}
