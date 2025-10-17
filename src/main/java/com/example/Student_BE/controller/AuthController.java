package com.example.Student_BE.controller;

import com.example.Student_BE.dto.*;
import com.example.Student_BE.utils.ApiResponse;
import com.example.Student_BE.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý authentication
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API đăng nhập")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * API đăng nhập
     */
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Đăng nhập với username và password")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            LoginResponseDto response = authService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    @PostMapping("/register")
    @Operation(summary = "Đăng ky", description = "Đăng ky voi username va password")
    public ResponseEntity<ApiResponse<RegisterResponeDto>> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        try {
            authService.register(registerRequest);
            RegisterResponeDto response = new RegisterResponeDto();
            return ResponseEntity.ok(ApiResponse.success("Đăng ky thành công", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}
