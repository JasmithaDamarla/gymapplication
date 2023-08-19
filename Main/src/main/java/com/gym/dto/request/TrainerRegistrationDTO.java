package com.gym.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationDTO {
	
	@NotBlank(message = "FirstName should not be empty")
	private String firstName;
	@NotBlank(message = "LastName should not be empty")
	private String lastName;
	@Email
	private String email;
	@NotBlank(message = "Specialization should not be empty")
	private String specialization;
}
