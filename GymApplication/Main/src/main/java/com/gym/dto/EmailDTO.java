package com.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDTO {
	
	private String id;
	@Email
	@NotBlank(message = "from email should not be empty")
	private String fromEmail;
	@Email
	@NotBlank(message = "to email should not be empty")
	private String toEmail;
	@Email
	@NotBlank(message = "cc email should not be empty")
	private String ccEmail;
	@NotBlank(message = "subject should not be empty")
	private String subject;
	@NotBlank(message = "provide some body")
	private String body;
	@Pattern(regexp = "^(NEW|ACTIVE|FAIL)$", message = "Status must be either 'NEW', 'ACTIVE' or 'FAIL'")
	private String status;
	@NotBlank(message="remarks should not be empty")
	private String remarks;
}
