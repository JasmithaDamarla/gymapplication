package com.gym.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="emailNoti")
public class EmailNoti {
	
	@Id
	private String id;
	private String fromEmail;
	private String toEmail;
	private String ccEmail;
	private String subject;
	private String body;
	private String status;
	private String remarks;

}
