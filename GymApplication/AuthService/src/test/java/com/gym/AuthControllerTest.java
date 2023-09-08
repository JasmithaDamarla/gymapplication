package com.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.gym.controller.AuthController;
import com.gym.dto.AuthRequest;
import com.gym.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@InjectMocks
    private AuthController authcontroller;
    @Mock
    private AuthService service;
    @Mock
    private AuthenticationManager authenticationManager;

	@Test
	void testValidateToken() {
		doNothing().when(service).validateToken("User");
		assertEquals("Token is valid", authcontroller.validateToken("User"));
	}

	@Test
    void testGetToken() {
        when(authenticationManager.authenticate(any())).thenReturn(new Authentication() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public String getName() {
                return null;
            }
            
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                
            }
            
            @Override
            public boolean isAuthenticated() {
                return true;
            }
            
            @Override
            public Object getPrincipal() {
                return null;
            }
            
            @Override
            public Object getDetails() {
                return null;
            }
            
            @Override
            public Object getCredentials() {
                return null;
            }
            
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }
        });
        assertNull(authcontroller.getToken(new AuthRequest("User","1234")));
    }

	@Test
    void testGetTokenWithInvalidAuthentication() {
        when(authenticationManager.authenticate(any())).thenReturn(new Authentication() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public String getName() {
                return null;
            }
            
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                
            }
            
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            
            @Override
            public Object getPrincipal() {
                return null;
            }
            
            @Override
            public Object getDetails() {
                return null;
            }
            
            @Override
            public Object getCredentials() {
                return null;
            }
            
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }
        });
        assertThrows(RuntimeException.class,()->authcontroller.getToken(new AuthRequest("User","1234")));
    }

//    @Test
//    void getTokenValidCredentials() throws Exception {
//        AuthRequest authRequest = new AuthRequest("username", "password");
//        Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
//        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
////        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
//        Mockito.when(authService.generateToken(authRequest.getUsername())).thenReturn("generated-token");
//
//        mockMvc.perform(post("/auth/token")
//                .content(new ObjectMapper().writeValueAsString(authRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//    
////    @Test
////    void testTokenValidation() throws Exception {
////        String validToken = "validTokenValue";
////        Mockito.doNothing().when(authService).validateToken(anyString());
////        mockMvc.perform(get("/auth/validate")
////                .param("token", validToken))
////                .andExpect(status().isOk());
////    }
//
//    @Test
//    void testInvalidTokenValidation() throws Exception {
//        String invalidToken = "invalidTokenValue";
//        Mockito.doNothing().when(authService).validateToken(anyString());
//        mockMvc.perform(get("/auth/validate")
//                .param("token", invalidToken))
//                .andExpect(status().isUnauthorized());
//    }

}