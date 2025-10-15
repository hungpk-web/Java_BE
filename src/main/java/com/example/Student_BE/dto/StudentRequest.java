package com.example.Student_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO cho request tạo/cập nhật student
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request tạo/cập nhật student")
public class StudentRequest {
    
    @NotBlank(message = "Tên sinh viên không được để trống")
    @Size(max = 20, message = "Tên sinh viên không được quá 20 ký tự")
    @Schema(description = "Tên sinh viên", example = "Nguyễn Văn A")
    private String studentName;
    
    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(max = 10, message = "Mã sinh viên không được quá 10 ký tự")
    @Schema(description = "Mã sinh viên", example = "SV001")
    private String studentCode;
    
    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được quá 255 ký tự")
    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;
    
    @NotNull(message = "Điểm trung bình không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm trung bình phải >= 0")
    @DecimalMax(value = "10.0", message = "Điểm trung bình phải <= 10")
    @Schema(description = "Điểm trung bình", example = "8.5")
    private Double averageScore;
    
    @NotNull(message = "Ngày sinh không được để trống")
    @Schema(description = "Ngày sinh", example = "2000-01-01T00:00:00")
    private LocalDateTime dateOfBirth;
}
