package com.gym.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(name="trainee")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trainee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private LocalDate dob;
	private String address;
	
	@ManyToMany(mappedBy = "trainees")
	private List<Trainer> trainers;
	
	@OneToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;

	@Override
	public String toString(){
		return user.getUserName();
	}
}
