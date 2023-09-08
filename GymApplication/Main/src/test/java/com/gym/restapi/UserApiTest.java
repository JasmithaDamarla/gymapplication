package com.gym.restapi;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.dto.DetailsDTO;
import com.gym.dto.request.UpdatePasswordDTO;
import com.gym.service.interfaces.UserService;

@WebMvcTest(UserApi.class)
class UserApiTest {

	@MockBean
	private UserService userService;
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void loginTest() throws Exception {
		DetailsDTO dto = new DetailsDTO("ben", "56fAb#%&&");
		Mockito.when(userService.login(any(DetailsDTO.class))).thenReturn(true);
		mockMvc.perform(post("/main/user/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(dto))).andExpect(status().isOk());
	}
	
	@Test
	void updatePassword() throws Exception {
		UpdatePasswordDTO dto = new UpdatePasswordDTO("user","oldPassword","newPassword");
		Mockito.doNothing().when(userService).updatePassword(any(UpdatePasswordDTO.class));
		mockMvc.perform(put("/main/user/updatePassword").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(dto))).andExpect(status().isOk());
	}
}
