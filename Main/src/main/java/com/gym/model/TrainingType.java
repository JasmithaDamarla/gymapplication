package com.gym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(name="trainingType")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TrainingType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String trainingTypeName;
	
}
