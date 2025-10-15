package com.example.Student_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO cho response API chung
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response API chung")
public class ApiResponse<T> {
    
    @Schema(description = "Mã trạng thái", example = "200")
    private Integer status;
    
    @Schema(description = "Thông báo", example = "Thành công")
    private String message;
    
    @Schema(description = "Dữ liệu trả về")
    private T data;
    
    @Schema(description = "Thời gian phản hồi", example = "2024-01-01T00:00:00")
    private String timestamp;
    
    /**
     * Constructor cho response thành công
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Thành công", data, java.time.LocalDateTime.now().toString());
    }
    
    /**
     * Constructor cho response thành công với message tùy chỉnh
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, java.time.LocalDateTime.now().toString());
    }
    
    /**
     * Constructor cho response lỗi
     */
    public static <T> ApiResponse<T> error(Integer status, String message) {
        return new ApiResponse<>(status, message, null, java.time.LocalDateTime.now().toString());
    }
    public static <T> ApiResponse<T> error(Integer status, String message, T data) {
        return new ApiResponse<>(status, message, data, java.time.LocalDateTime.now().toString());
    }
}
