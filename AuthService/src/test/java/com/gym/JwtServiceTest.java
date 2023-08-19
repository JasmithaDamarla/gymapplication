/*
package com.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gym.service.JwtService;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
	
	@InjectMocks
    private JwtService jwtService;
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    
    
    @Test
    void testCreateToken() {
        String token = jwtService.generateToken("user");
        assertEquals(token.toString(), token);
    }

    @Test
    void testGetSignKey() {
        assertEquals("javax.crypto.spec.SecretKeySpec@fa77f4ac", jwtService.getSignKey().toString());
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken("user");
        assertEquals(token.toString(), token);
    }

}
*/
