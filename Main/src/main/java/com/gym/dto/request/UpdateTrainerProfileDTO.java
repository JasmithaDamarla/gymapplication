package com.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerProfileDTO {

	@NotBlank(message = "userName should not be empty")
	private String userName;
	@NotBlank(message = "FirstName should not be empty")
	private String firstName;
	@NotBlank(message = "LastName should not be empty")
	private String lastName;
	@Pattern(regexp = "^(ZUMBA|FITNESS|YOGA|STRETCHING|RESISTANCE)$", message = "Specialization must be either 'ZUMBA' or 'FITNESS' or 'YOGA' or 'STRETCHING' or 'RESISTANCE'")
	private String specialization;
	private boolean isActive;
}
