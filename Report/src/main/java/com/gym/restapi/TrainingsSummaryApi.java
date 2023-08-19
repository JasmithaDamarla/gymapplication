package com.gym.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.TrainingsSummaryDTO;
import com.gym.model.TrainingsSummary;
import com.gym.service.TrainingsSummaryService;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/reports")
@RestController
public class TrainingsSummaryApi {

	@Autowired
	private TrainingsSummaryService trainingsSummaryService;
	
	@PostMapping("/allTrainingsSummary")
	public ResponseEntity<Void> addAllTrainingSummary(@RequestBody List<TrainingsSummaryDTO> trainings) {
		log.info("Entered report service");
		log.info("summary {}",trainings);
		trainingsSummaryService.addAllTrainingsSummary(trainings);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/trainingsSummary")
	public ResponseEntity<Void> addTrainingSummary(@RequestBody TrainingsSummaryDTO trainingSummaryDTO) {
		log.info("Entered report service");
		log.info("summary {}",trainingSummaryDTO);
		trainingsSummaryService.addTrainingsSummary(trainingSummaryDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/trainingsSummary")
	public void getTrainingSummary(HttpServletResponse response) throws Exception {
		String filename = "Trainers_Trainings_summary_YYYY_MM.csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
		StatefulBeanToCsv<TrainingsSummary> writer = new StatefulBeanToCsvBuilder<TrainingsSummary>(
				response.getWriter()).withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
				.withSeparator(ICSVWriter.DEFAULT_SEPARATOR).build();
		log.info("generating summaryy");
		writer.write(trainingsSummaryService.getTrainingsSummary());
		
	}
}
