package com.gym.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="trainings")
public class TrainingsSummary {
	
	@Id
	private String trainerUserName;
	private String trainerFirstName;
	private String trainerLastName;
	private String trainerStatus;
	private LocalDate date;

}
