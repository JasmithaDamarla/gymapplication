package com.gym.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.DetailsDTO;
import com.gym.dto.request.UpdatePasswordDTO;
import com.gym.exceptions.UserException;
import com.gym.service.interfaces.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RequestMapping("/main/user")
@RestController
public class UserApi {

	@Autowired
	private UserService userService;

	@PutMapping("updatePassword")
	public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO)
			throws UserException {
		log.info("updating password for username {} ", updatePasswordDTO.getUserName());
		userService.updatePassword(updatePasswordDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("login")
	public ResponseEntity<String> login(@Valid @RequestBody DetailsDTO details) throws UserException {
		log.info("logging in the user");
		userService.login(details);
		return new ResponseEntity<>("Logged in Successfully",HttpStatus.OK);

	}

}
