package com.example.Student_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO cho response đăng nhập
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response đăng nhập")
public class LoginResponse {

    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Loại token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Thời điểm đăng nhập (yyyy-MM-dd HH:mm:ss)", example = "2025-10-14 13:30:00")
    private String loginAt;

    @Schema(description = "Thời điểm hết hạn token (yyyy-MM-dd HH:mm:ss)", example = "2025-10-14 14:30:00")
    private String expiresAt;

    @Schema(description = "Mã người dùng", example = "1")
    private Integer userId;

    @Schema(description = "Tên đăng nhập", example = "admin")
    private String userName;
}
