package com.example.Student_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO cho response student
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response thông tin student")
public class StudentResponseDto {
    
    @Schema(description = "ID sinh viên", example = "1")
    private Integer studentId;
    
    @Schema(description = "Tên sinh viên", example = "Nguyễn Văn A")
    private String studentName;
    
    @Schema(description = "Mã sinh viên", example = "SV001")
    private String studentCode;
    
    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;
    
    @Schema(description = "Điểm trung bình", example = "8.5")
    private Double averageScore;
    
    @Schema(description = "Ngày sinh", example = "2000-01-01T00:00:00")
    private LocalDateTime dateOfBirth;
}
