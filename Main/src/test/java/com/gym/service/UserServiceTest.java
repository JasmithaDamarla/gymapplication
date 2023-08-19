package com.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gym.dto.DetailsDTO;
import com.gym.dto.request.UpdatePasswordDTO;
import com.gym.exceptions.UserException;
import com.gym.model.User;
import com.gym.repository.UserRepository;
import com.gym.service.implementation.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Test
	void updatePasswordSuccessTest() {
		UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("ben","ben^78a","ben@123");
		User savedUser = new User(1, "ben", "stone", "benstone", "ben^78a", false, "benstone@gmail.com");
		Mockito.when(userRepository.findByUserName(updatePasswordDTO.getUserName())).thenReturn(Optional.of(savedUser));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);
		userServiceImpl.updatePassword(updatePasswordDTO);
		
		assertEquals(updatePasswordDTO.getNewPassword(), savedUser.getPassword());
		Mockito.verify(userRepository, times(1)).findByUserName(anyString());
		Mockito.verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void updatePasswordUserNotFound() {
		UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
		updatePasswordDTO.setUserName("user");
		updatePasswordDTO.setNewPassword("User@123");
		Mockito.when(userRepository.findByUserName(updatePasswordDTO.getUserName())).thenReturn(Optional.empty());
		
		assertThrows(UserException.class, () -> userServiceImpl.updatePassword(updatePasswordDTO));
		Mockito.verify(userRepository, times(1)).findByUserName(updatePasswordDTO.getUserName());
		Mockito.verify(userRepository, never()).save(any(User.class));
	}
	
	@Test
	void updatePasswordFail() {
		UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
		updatePasswordDTO.setUserName("user");
		updatePasswordDTO.setNewPassword("User@123");
		Mockito.when(userRepository.findByUserName(updatePasswordDTO.getUserName())).thenReturn(Optional.of(new User()));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(null);
		assertThrows(UserException.class, () -> userServiceImpl.updatePassword(updatePasswordDTO));
		Mockito.verify(userRepository, times(1)).findByUserName(updatePasswordDTO.getUserName());
		Mockito.verify(userRepository, times(1)).save(any(User.class));
	}
	
	@Test
	void loginSuccess() {
		DetailsDTO login = new DetailsDTO("user","User@123");
		User savedUser = new User(1, "user", "user", "user", "User@123", false, "user@gmail.com"); 
		Mockito.when(userRepository.findByUserName(login.getUserName())).thenReturn(Optional.of(savedUser));
		boolean exp = userServiceImpl.login(login);
		Mockito.verify(userRepository, times(1)).findByUserName(login.getUserName());
        assertTrue(exp);
	}
	
	@Test
	void loginUserNotFound() {
		DetailsDTO login = new DetailsDTO("user","User@123"); 
		Mockito.when(userRepository.findByUserName(login.getUserName())).thenReturn(Optional.empty());
		assertThrows(UserException.class, () -> userServiceImpl.login(login));
		Mockito.verify(userRepository, times(1)).findByUserName(login.getUserName());
	}
	
	@Test
	void loginFail() {
		DetailsDTO login = new DetailsDTO("user","User@123");
		User savedUser = new User(1, "user", "user", "user", "adkaser@123", false, "user@gmail.com"); 
		Mockito.when(userRepository.findByUserName(login.getUserName())).thenReturn(Optional.of(savedUser));
		assertThrows(UserException.class,()->{
			userServiceImpl.login(login);
		});
		Mockito.verify(userRepository, times(1)).findByUserName(login.getUserName());
	}
}
