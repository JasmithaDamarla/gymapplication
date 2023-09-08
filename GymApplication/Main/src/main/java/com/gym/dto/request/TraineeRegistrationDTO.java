package com.gym.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRegistrationDTO {

	@NotBlank(message = "FirstName should not be empty")
	private String firstName;
	@NotBlank(message = "LastName should not be empty")
	private String lastName;
	@Email
	@NotBlank(message = "email should not be empty")
	private String email;
	private LocalDate dob;
	private String address;
}
