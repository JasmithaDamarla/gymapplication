package com.gym.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@Table(name = "trainer")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Trainer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trainee2Trainer", joinColumns = @JoinColumn(name = "trainer_id"), inverseJoinColumns = @JoinColumn(name = "trainee_id"),uniqueConstraints = @UniqueConstraint(columnNames = {"trainer_id","trainee_id"}))
	private Set<Trainee> trainees;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "trainingType_id", referencedColumnName = "id")
	private TrainingType trainingType;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trainer other = (Trainer) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString(){
		return user.getUserName();
	}

}
