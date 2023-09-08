package com.gym.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileResponseDTO {

	private String userName;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String address;
	private boolean isActive;
	private List<TrainerProfile> trainersList;
}
