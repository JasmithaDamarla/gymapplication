package com.gym.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.gym.entity.User;
import com.gym.repository.UserCredentialRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserCredentialRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> credential = repository.findByUserName(username);
		log.info(credential.toString());
		return credential.map(User::new)
				.orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
	}
}
