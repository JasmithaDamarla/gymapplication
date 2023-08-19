package com.gym.service;
import com.gym.dto.EmailDTO;

import jakarta.mail.MessagingException;

public interface EmailService {
	void sendEmail(EmailDTO emailDTO) throws MessagingException;
}
