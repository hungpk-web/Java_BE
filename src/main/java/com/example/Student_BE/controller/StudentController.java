package com.example.Student_BE.controller;

import com.example.Student_BE.dto.ApiResponse;
import com.example.Student_BE.dto.StudentRequest;
import com.example.Student_BE.dto.StudentResponse;
import com.example.Student_BE.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý Student APIs
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "API quản lý sinh viên")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * API lấy danh sách tất cả student
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách sinh viên", description = "Lấy danh sách tất cả sinh viên")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents() {
        try {
            List<StudentResponse> students = studentService.getAllStudents();
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sinh viên thành công ", students));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    /**
     * API lấy student theo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin sinh viên", description = "Lấy thông tin sinh viên theo ID")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(
            @Parameter(description = "ID sinh viên") @PathVariable Integer id) {
        try {
            StudentResponse student = studentService.getStudentById(id);
            return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sinh viên thành công", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    /**
     * API tạo student mới
     */
    @PostMapping
    @Operation(summary = "Tạo sinh viên mới", description = "Tạo sinh viên mới với thông tin đầy đủ")
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@Valid @RequestBody StudentRequest studentRequest) {
        try {
            StudentResponse student = studentService.createStudent(studentRequest);
            return ResponseEntity.ok(ApiResponse.success("Tạo sinh viên thành công", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    /**
     * API cập nhật student
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật sinh viên", description = "Cập nhật thông tin sinh viên theo ID")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @Parameter(description = "ID sinh viên") @PathVariable Integer id,
            @Valid @RequestBody StudentRequest studentRequest) {
        try {
            StudentResponse student = studentService.updateStudent(id, studentRequest);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật sinh viên thành công", student));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    /**
     * API xóa student
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa sinh viên", description = "Xóa sinh viên theo ID")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(
            @Parameter(description = "ID sinh viên") @PathVariable Integer id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa sinh viên thành công", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

}
