package com.gym;

import java.security.SecureRandom;

import org.apache.commons.lang.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableFeignClients
@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

//	@Bean
//	public String PasswordGenerator() {
//		char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=")
//				.toCharArray();
//		return RandomStringUtils.random(10, 0, possibleCharacters.length - 1, false, false, possibleCharacters,
//				new SecureRandom());
//	}
	
	 @Bean
		public PasswordEncoder passwordEncoder(){
			return new BCryptPasswordEncoder();
		}
}
