package com.gym.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gym.dto.TrainingsSummaryDTO;

@FeignClient(name = "report-service/reports")
public interface ReportProxy {

	@PostMapping("/trainingsSummary")
	public ResponseEntity<Void> addTrainingSummary(@RequestBody TrainingsSummaryDTO training);
}
