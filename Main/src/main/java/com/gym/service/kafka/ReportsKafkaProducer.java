package com.gym.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.gym.dto.TrainingsSummaryDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportsKafkaProducer {

	@Autowired
	private KafkaTemplate<String, TrainingsSummaryDTO> reportsKafkaTemplate;
	
	public void addTrainingSummary(TrainingsSummaryDTO trainingSummary) throws KafkaException{
		log.info(String.format("Message sent -> %s", trainingSummary.toString()));
		reportsKafkaTemplate.send("reports", trainingSummary);
	}
}
