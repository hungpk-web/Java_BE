package com.example.Student_BE.controller;

import com.example.Student_BE.dto.*;
import com.example.Student_BE.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private LoginRequestDto loginRequest;
    private LoginResponseDto loginResponse;



    @BeforeEach
    void initData() {
        // Setup LoginRequest
        loginRequest = new LoginRequestDto();
        loginRequest.setUserName("admin");
        loginRequest.setPassword("123456");

        // Setup LoginResponse
        loginResponse = new LoginResponseDto();
        loginResponse.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
        loginResponse.setTokenType("Bearer");
        loginResponse.setLoginAt("2025-01-10 10:30:00");
        loginResponse.setExpiresAt("2025-01-10 11:30:00");
        loginResponse.setUserId(1);
        loginResponse.setUserName("admin");

    }

    @Test
    void login_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(loginRequest);

        Mockito.when(authService.login(ArgumentMatchers.any()))
                .thenReturn(loginResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Đăng nhập thành công"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("data.userName").value("admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").exists());
    }

    @Test
    void login_invalidCredentials_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(loginRequest);

        Mockito.when(authService.login(ArgumentMatchers.any()))
                .thenThrow(new RuntimeException("Username hoặc password không đúng"));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username hoặc password không đúng"))
                .andExpect(MockMvcResultMatchers.jsonPath("data")
                        .isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp")
                        .exists());
    }

    @Test
    void login_usernameBlank_fail() throws Exception {
        // GIVEN
        loginRequest.setUserName("");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(loginRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Dữ liệu không hợp lệ"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.userName").value("Username không được để trống"))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").exists());
    }

    @Test
    void login_passwordTooShort_fail() throws Exception {
        // GIVEN
        loginRequest.setPassword("123");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(loginRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Dữ liệu không hợp lệ"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.password").value("Password không được quá 15 ký tự"))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").exists());
    }

    @Test
    void login_multipleValidationErrors_fail() throws Exception {
        // GIVEN
        loginRequest.setUserName(""); // Invalid
        loginRequest.setPassword("123"); // Invalid
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(loginRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Dữ liệu không hợp lệ"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.userName").value("Username không được để trống"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.password").value("Password không được quá 15 ký tự"))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").exists());
    }

    @Test
    void login_usernameTooLong_fail() throws Exception {
        // GIVEN
        loginRequest.setUserName("verylongusernamethatexceedslimit");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(loginRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Dữ liệu không hợp lệ"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.userName").value("Username không được quá 20 ký tự"))
                .andExpect(MockMvcResultMatchers.jsonPath("timestamp").exists());
    }

}
