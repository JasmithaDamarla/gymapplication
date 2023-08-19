package com.gym.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Training {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "trainee_id", referencedColumnName = "id")
	private Trainee trainee;

	@ManyToOne
	@JoinColumn(name = "trainer_id", referencedColumnName = "id")
	private Trainer trainer;

	private String name;

	@ManyToOne
	@JoinColumn(name = "specilization_id", referencedColumnName = "id")
	private TrainingType trainingType;

	private LocalDate date;
	private int duration;

}
