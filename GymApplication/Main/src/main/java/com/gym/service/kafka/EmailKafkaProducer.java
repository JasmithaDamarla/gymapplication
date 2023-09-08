package com.gym.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.gym.dto.EmailDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailKafkaProducer {

	@Autowired
	private KafkaTemplate<String, EmailDTO> emailKafkaTemplate;
	
	public void sendEmail(EmailDTO mailDTO) throws KafkaException{
		log.info(String.format("Message sending -> %s", mailDTO.toString()));
		emailKafkaTemplate.send("email", mailDTO);
	}
}
