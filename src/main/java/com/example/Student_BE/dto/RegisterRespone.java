package com.example.Student_BE.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response đăng ky")
public class RegisterRespone {
    @Schema(description = "ID user", example = "1")
    private Integer userId;

    @Schema(description = "Tên dang nhap", example = "user2")
    private String userName;

    @Schema(description = "Mat khau", example = "123456")
    private String password;
}
