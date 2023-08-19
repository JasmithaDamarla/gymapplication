package com.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {

	@NotBlank(message = "UserName should not be empty")
	private String userName;
	@NotBlank(message = "Old Password should not be empty")
	private String oldPassword;
	@NotBlank(message = "New Password should not be empty")
	private String newPassword;
}
