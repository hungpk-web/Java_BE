package com.example.Student_BE.controller;

import com.example.Student_BE.dto.StudentRequestDto;
import com.example.Student_BE.dto.StudentResponseDto;
import com.example.Student_BE.service.StudentService;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.Student_BE.utils.getToken;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    private StudentRequestDto studentRequest;
    private StudentResponseDto studentResponse;
    private List<StudentResponseDto> studentList;
    private final String  token=getToken.token;
    @BeforeEach
    void initData() {
        // Setup StudentRequest
        studentRequest = new StudentRequestDto();
        studentRequest.setStudentName("Nguyễn Văn A");
        studentRequest.setStudentCode("SV001");
        studentRequest.setAddress("123 Đường ABC, Quận 1, TP.HCM");
        studentRequest.setAverageScore(8.5);
        studentRequest.setDateOfBirth(LocalDateTime.of(2000, 1, 1, 0, 0, 0));

        // Setup StudentResponse
        studentResponse = new StudentResponseDto();
        studentResponse.setStudentId(1);
        studentResponse.setStudentName("Nguyễn Văn A");
        studentResponse.setStudentCode("SV001");
        studentResponse.setAddress("123 Đường ABC, Quận 1, TP.HCM");
        studentResponse.setAverageScore(8.5);
        studentResponse.setDateOfBirth(LocalDateTime.of(2000, 1, 1, 0, 0, 0));

        // Setup Student List
        StudentResponseDto student2 = new StudentResponseDto();
        student2.setStudentId(2);
        student2.setStudentName("Trần Thị B");
        student2.setStudentCode("SV002");
        student2.setAddress("456 Đường XYZ, Quận 2, TP.HCM");
        student2.setAverageScore(9.0);
        student2.setDateOfBirth(LocalDateTime.of(2001, 2, 2, 0, 0, 0));

        studentList = Arrays.asList(studentResponse, student2);
    }

    @Test
    void getAllStudents_success() throws Exception {
        // GIVEN
        Mockito.when(studentService.getAllStudents())
                .thenReturn(studentList);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/students")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Lấy danh sách sinh viên thành công "))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].studentId")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].studentName")
                        .value("Nguyễn Văn A"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].studentId")
                        .value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].studentName")
                        .value("Trần Thị B"));
    }

    @Test
    void getAllStudents_serviceError_fail() throws Exception {
        // GIVEN
        Mockito.when(studentService.getAllStudents())
                .thenThrow(new RuntimeException("Lỗi kết nối database"));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/students")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Lỗi kết nối database"));
    }

    @Test
    void getStudentById_validId_success() throws Exception {
        // GIVEN
        Integer studentId = 1;
        Mockito.when(studentService.getStudentById(studentId))
                .thenReturn(studentResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/students/{id}", studentId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Lấy thông tin sinh viên thành công"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentId")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentName")
                        .value("Nguyễn Văn A"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentCode")
                        .value("SV001"));
    }

    @Test
    void getStudentById_studentNotFound_fail() throws Exception {
        // GIVEN
        Integer studentId = 999;
        Mockito.when(studentService.getStudentById(studentId))
                .thenThrow(new RuntimeException("Student không tồn tại với ID: " + studentId));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/students/{id}", studentId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Student không tồn tại với ID: 999"));
    }

    @Test
    void createStudent_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(studentRequest);

        Mockito.when(studentService.createStudent(ArgumentMatchers.any()))
                .thenReturn(studentResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Tạo sinh viên thành công"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentId")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentName")
                        .value("Nguyễn Văn A"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentCode")
                        .value("SV001"));
    }

    @Test
    void createStudent_duplicateStudentCode_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(studentRequest);

        Mockito.when(studentService.createStudent(ArgumentMatchers.any()))
                .thenThrow(new RuntimeException("Mã sinh viên đã tồn tại"));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/students")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Mã sinh viên đã tồn tại"));
    }

    @Test
    void createStudent_studentNameValid_fail() throws Exception {
        // GIVEN
        studentRequest.setStudentName(""); // Invalid: empty name
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(studentRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/students")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Dữ liệu không hợp lệ"));

    }

    @Test
    void createStudent_averageScoreInvalid_fail() throws Exception {
        // GIVEN
        studentRequest.setAverageScore(15.0); // Invalid: score > 10
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(studentRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/students")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Dữ liệu không hợp lệ"));
    }

    @Test
    void updateStudent_validRequest_success() throws Exception {
        // GIVEN
        Integer studentId = 1;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(studentRequest);

        Mockito.when(studentService.updateStudent(ArgumentMatchers.eq(studentId), ArgumentMatchers.any()))
                .thenReturn(studentResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/students/{id}", studentId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Cập nhật sinh viên thành công"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentId")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("data.studentName")
                        .value("Nguyễn Văn A"));
    }


    @Test
    void deleteStudent_validId_success() throws Exception {
        // GIVEN
        Integer studentId = 1;
        Mockito.doNothing().when(studentService).deleteStudent(studentId);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/students/{id}", studentId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Xóa sinh viên thành công"));
    }

    @Test
    void deleteStudent_studentNotFound_fail() throws Exception {
        // GIVEN
        Integer studentId = 999;
        Mockito.doThrow(new RuntimeException("Student không tồn tại với ID: " + studentId))
                .when(studentService).deleteStudent(studentId);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/students/{id}", studentId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Student không tồn tại với ID: 999"));
    }
}
