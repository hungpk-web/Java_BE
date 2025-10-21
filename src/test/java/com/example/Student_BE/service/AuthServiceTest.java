package com.example.Student_BE.service;

import com.example.Student_BE.dao.UserDao;
import com.example.Student_BE.dto.LoginRequestDto;
import com.example.Student_BE.dto.LoginResponseDto;
import com.example.Student_BE.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequestDto LoginRequestDto;

    @BeforeEach
    void initData() {

        testUser = new User(1, "testuser", "encodedPassword");
        LoginRequestDto = new LoginRequestDto("testuser", "password123");


        ReflectionTestUtils.setField(authService, "expiration", 86400000L);
    }

    @Test
    void login_success() {
        // GIVEN
        when(userDao.findByUserName("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(1, "testuser")).thenReturn("jwt-token");

        // WHEN
        LoginResponseDto response = authService.login(LoginRequestDto);

        // THEN
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1, response.getUserId());
        assertEquals("testuser", response.getUserName());
        assertNotNull(response.getLoginAt());
        assertNotNull(response.getExpiresAt());

        verify(userDao).findByUserName("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtService).generateToken(1, "testuser");
    }

    @Test
    void login_userNotFound_throwsException() {
        // GIVEN
        when(userDao.findByUserName("testuser")).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(LoginRequestDto);
        });

        assertEquals("Username không tồn tại", exception.getMessage());
        verify(userDao).findByUserName("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any(), anyString());
    }

    @Test
    void login_wrongPassword_throwsException() {
        // GIVEN
        when(userDao.findByUserName("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(LoginRequestDto);
        });

        assertEquals("Password không đúng", exception.getMessage());
        verify(userDao).findByUserName("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtService, never()).generateToken(any(), anyString());
    }

    @Test
    void validateToken_success() {
        // GIVEN
        String token = "jwt-token";
        String username = "testuser";
        when(jwtService.validateToken(token, username)).thenReturn(true);

        // WHEN
        boolean result = authService.validateToken(token, username);

        // THEN
        assertTrue(result);
        verify(jwtService).validateToken(token, username);
    }

    @Test
    void validateToken_invalidToken_returnsFalse() {
        // GIVEN
        String token = "invalid-token";
        String username = "testuser";
        when(jwtService.validateToken(token, username)).thenReturn(false);

        // WHEN
        boolean result = authService.validateToken(token, username);

        // THEN
        assertFalse(result);
        verify(jwtService).validateToken(token, username);
    }

}
