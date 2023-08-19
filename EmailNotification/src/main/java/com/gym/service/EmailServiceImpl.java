package com.gym.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.gym.dto.EmailDTO;
import com.gym.model.EmailNoti;
import com.gym.repository.EmailNotificationRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private EmailNotificationRepository emailNotificationRepository;

	
//	@KafkaListener(topics = "${gym.email-topic}", groupId="gymGrp")
	@Override
	public void sendEmail(EmailDTO emailDTO) throws MessagingException {
		log.info(String.format("Json msg recevived -> %s", emailDTO.toString()));
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setSubject(emailDTO.getSubject());
		helper.setCc(emailDTO.getCcEmail());
		helper.setFrom(emailDTO.getFromEmail());
		helper.setTo(emailDTO.getToEmail());
		helper.setText(emailDTO.getBody());
        mailSender.send(message);
        log.info("Sent successfullyy!!");
        emailNotificationRepository.save(modelMapper.map(emailDTO, EmailNoti.class));
	}

}
