package com.gym.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.dto.DetailsDTO;
import com.gym.dto.request.UpdatePasswordDTO;
import com.gym.exceptions.UserException;
import com.gym.model.User;
import com.gym.repository.UserRepository;
import com.gym.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void updatePassword(UpdatePasswordDTO updatePasswordDTO) throws UserException {
		User user = userRepository.findByUserName(updatePasswordDTO.getUserName())
				.orElseThrow(() -> new UserException("Could not able to find user of given username"));
		user.setPassword(updatePasswordDTO.getNewPassword());
		log.info("fetched record and updating password");
		User savedUser = Optional.ofNullable(user).map(userRepository::save)
				.orElseThrow(() -> new UserException("Unable to update user"));
		log.info("updated user {} successfully ", savedUser);
	}

	@Override
	public boolean login(DetailsDTO login) throws UserException {
		User user = userRepository.findByUserName(login.getUserName())
				.orElseThrow(() -> new UserException("No such user exception"));
		if(user.getPassword().equals(login.getPassword())) {
			log.info("Logged in successfully");
			return true;
		} else {
			throw new UserException("Invalid credentails");
		}

	}
}
