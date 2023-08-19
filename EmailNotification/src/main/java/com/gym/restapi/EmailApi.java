package com.gym.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.dto.EmailDTO;
import com.gym.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/email")
@RestController
public class EmailApi {

	@Autowired
	private EmailService mailService;

	@PostMapping("/sendMail")
	public void sendEmail(@RequestBody EmailDTO emailDTO) throws MessagingException {
		log.info("entered email service {}", emailDTO);
		mailService.sendEmail(emailDTO);
	}
}
