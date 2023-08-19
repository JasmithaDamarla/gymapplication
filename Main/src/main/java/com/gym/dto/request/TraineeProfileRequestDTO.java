package com.gym.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileRequestDTO {
	
	@NotBlank(message = "userName should not be empty")
	private String userName;
	@NotBlank(message = "FirstName should not be empty")
	private String firstName;
	@NotBlank(message = "LastName should not be empty")
	private String lastName;
	private LocalDate dob;
	private String address;
	private boolean isActive;

}
