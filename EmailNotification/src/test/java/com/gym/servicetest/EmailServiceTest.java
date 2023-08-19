package com.gym.servicetest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;

import com.gym.dto.EmailDTO;
import com.gym.model.EmailNoti;
import com.gym.repository.EmailNotificationRepository;
import com.gym.service.EmailServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

	@Mock(lenient = true)
	private JavaMailSender mockMailSender;
	@Mock
	private ModelMapper mockModelMapper;
	@Mock
	private EmailNotificationRepository mockEmailNotificationRepository;
	@InjectMocks
	private EmailServiceImpl emailServiceImpl;

	@Test
	void sendEmailSuccess() throws MessagingException {
		EmailDTO emailDTO = new EmailDTO("name", "from@gmail.com", "to@gmail.com", "cc@gmail.com", "Greet", "Hi", "NEW",
				"Nrml");
		EmailNoti emailNoti = new EmailNoti("name", "from@gmail.com", "to@gmail.com", "cc@gmail.com", "Greet", "Hi",
				"NEW", "Nrml");
		Mockito.when(mockModelMapper.map(emailDTO, EmailNoti.class)).thenReturn(emailNoti);
		ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
		Mockito.when(mockMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
		emailServiceImpl.sendEmail(emailDTO);
		Mockito.verify(mockMailSender).send(mimeMessageCaptor.capture());
		Mockito.verify(mockEmailNotificationRepository).save(emailNoti);
	}

	@Test
	void sendEmailException() throws MessagingException {
		EmailDTO emailDTO = new EmailDTO("name", "from@gmail.com", "to@gmail.com", "", "Greet", "Hi", "NEW",
				"Nrml");
		Mockito.when(mockMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
		Mockito.doNothing().when(mockMailSender).send(any(MimeMessage.class));
		assertThrows(MessagingException.class, () -> {
			emailServiceImpl.sendEmail(emailDTO);
		});
		
	}
}
