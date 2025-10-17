package com.example.Student_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO cho request đăng ky
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request đăng ky")
public class RegisterRequestDto {

    @NotBlank(message = "Username không được để trống")
    @Size(max = 20, message = "Username không được quá 20 ký tự")
    @Schema(description = "Tên đăng nhập", example = "admin")
    private String userName;

    @NotBlank(message = "Password không được để trống")
    @Size(max = 15, min= 6,message = "Password phai tu 6 den 15 ký tự")
    @Schema(description = "Mật khẩu", example = "123456")
    private String password;

    @NotBlank(message = "Confirm Password không được để trống")
    @Size(max = 15, min =6,message = "Confirm Password phai tu 6 den 15 ký tự")
    @Schema(description = "Mật khẩu xac nhan ", example = "123456")
    private String confirmPassword;
}