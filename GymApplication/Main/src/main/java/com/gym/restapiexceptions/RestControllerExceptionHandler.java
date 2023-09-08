package com.gym.restapiexceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.gym.exceptions.TraineeException;
import com.gym.exceptions.TrainerException;
import com.gym.exceptions.UserException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException func, WebRequest req) {
		List<String> inpErrors = new ArrayList<>();
		func.getAllErrors()
			.forEach(error -> 
				inpErrors.add(error.getDefaultMessage()));
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(), inpErrors.toString(), req.getDescription(false));
		log.info("Method arugument not valid exception raised : {}",inpErrors.toString());
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleUserException(UserException userException, WebRequest req) {
		log.info("UserException exception has been raised : {}",userException.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),userException.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TraineeException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleTraineeException(TraineeException traineeException, WebRequest req) {
		log.info("trainee exception has been raised : {}",traineeException.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),traineeException.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(KafkaException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleKafkaException(KafkaException kafkaException, WebRequest req) {
		log.info("trainee exception has been raised : {}",kafkaException.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),kafkaException.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TrainerException.class)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public ResponseEntity<ExceptionResponse> handleTrainerException(TrainerException trainerException, WebRequest req) {
		log.info("trainer exception has been raised : {}",trainerException.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),trainerException.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleSQLIntegerityException(DataIntegrityViolationException fail, WebRequest req) {
		log.info("sql integrity exception has been raised : {}",fail.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),fail.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException runtime, WebRequest req) {
		log.info("Run time exception has been raised : {}",runtime.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),runtime.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
