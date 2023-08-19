package com.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gym.service.AuthService;
import com.gym.service.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	
	    @InjectMocks
	    private AuthService authService;
	    @Mock
	    private JwtService jwtService;

	    @Test
	    void testGenerateToken() {
	        when(jwtService.generateToken("user")).thenReturn("user");
	        assertEquals("user",authService.generateToken("user"));
	    }

	    @Test
	    void testValidateToken() {
	        doNothing().when(jwtService).validateToken("user");
	        authService.validateToken("user");
	        verify(jwtService, times(1)).validateToken("user");
	    }

}

