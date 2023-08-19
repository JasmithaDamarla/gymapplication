package com.gym.restapitest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.dto.EmailDTO;
import com.gym.restapi.EmailApi;
import com.gym.service.EmailService;

@WebMvcTest(EmailApi.class)
class EmailApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Test
    void sendEmailSuccess() throws Exception {
        EmailDTO emailDTO = new EmailDTO();
        Mockito.doNothing().when(emailService).sendEmail(Mockito.any(EmailDTO.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/email/sendMail")
                .content(new ObjectMapper().writeValueAsString(emailDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.any(EmailDTO.class));
    }
}