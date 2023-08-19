package com.gym.service.interfaces;

import com.gym.dto.DetailsDTO;
import com.gym.dto.request.UpdatePasswordDTO;
import com.gym.exceptions.UserException;

public interface UserService {
	void updatePassword(UpdatePasswordDTO updatePasswordDTO) throws UserException;
	boolean login(DetailsDTO login) throws UserException;
}
