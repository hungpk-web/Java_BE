package com.example.Student_BE.service;

import com.example.Student_BE.dto.StudentRequestDto;
import com.example.Student_BE.dto.StudentResponseDto;
import com.example.Student_BE.entity.Student;
import com.example.Student_BE.entity.StudentInfo;
import com.example.Student_BE.dao.StudentDao;
import com.example.Student_BE.dao.StudentInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service xử lý business logic cho Student
 */
@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    /**
     * Lấy danh sách tất cả student
     */
    public List<StudentResponseDto> getAllStudents() {
        List<Student> students = studentDao.selectAll();
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy student theo ID
     */
    public StudentResponseDto getStudentById(Integer studentId) {
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }
        return convertToResponse(student);
    }

    /**
     * Tạo student mới
     */
    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto) {
        // Kiểm tra student code đã tồn tại chưa
        if (studentDao.existsByStudentCode(studentRequestDto.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại: " + studentRequestDto.getStudentCode());
        }

        // Tạo Student entity
        Student student = new Student(null, studentRequestDto.getStudentName(), studentRequestDto.getStudentCode());

        // Lưu Student và lấy entity với ID đã được tạo
        Student savedStudent = studentDao.insert(student).getEntity();

        // Tạo StudentInfo
        StudentInfo studentInfo = new StudentInfo(null, savedStudent.getStudentId(),
                studentRequestDto.getAddress(), studentRequestDto.getAverageScore(), studentRequestDto.getDateOfBirth());

        // Lưu StudentInfo
        studentInfoDao.insert(studentInfo).getEntity();

        return convertToResponse(savedStudent);
    }

    /**
     * Cập nhật student
     */
    public StudentResponseDto updateStudent(Integer studentId, StudentRequestDto studentRequestDto) {
        // Tìm student
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }

        // Kiểm tra student code có thay đổi và đã tồn tại chưa
        if (!student.getStudentCode().equals(studentRequestDto.getStudentCode())
                && studentDao.existsByStudentCode(studentRequestDto.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại: " + studentRequestDto.getStudentCode());
        }

        // Cập nhật thông tin Student
        Student updatedStudent = new Student(studentId, studentRequestDto.getStudentName(), studentRequestDto.getStudentCode());
        studentDao.update(updatedStudent).getEntity();

        // Cập nhật StudentInfo
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(studentId);
        if (studentInfoOpt.isPresent()) {
            StudentInfo studentInfo = studentInfoOpt.get();
            StudentInfo updatedStudentInfo = new StudentInfo(studentInfo.getInfoId(), studentId,
                    studentRequestDto.getAddress(), studentRequestDto.getAverageScore(), studentRequestDto.getDateOfBirth());
            studentInfoDao.update(updatedStudentInfo).getEntity();
        }

        return convertToResponse(updatedStudent);
    }

    /**
     * Xóa student
     */
    public void deleteStudent(Integer studentId) {
        // Kiểm tra student có tồn tại không
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }

        // Xóa StudentInfo trước (do foreign key constraint)
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(studentId);
        if (studentInfoOpt.isPresent()) {
            studentInfoDao.delete(studentInfoOpt.get()).getEntity();
        }

        // Xóa Student
        studentDao.delete(student).getEntity();
    }

    /**
     * Tìm kiếm student theo tên
     */
    public List<StudentResponseDto> searchStudentsByName(String name) {
        List<Student> students = studentDao.findByStudentNameContaining("%" + name + "%");
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Student entity to StudentResponse
     */
    private StudentResponseDto convertToResponse(Student student) {
        StudentResponseDto response = new StudentResponseDto();
        response.setStudentId(student.getStudentId());
        response.setStudentName(student.getStudentName());
        response.setStudentCode(student.getStudentCode());

        // Lấy thông tin từ StudentInfo
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(student.getStudentId());
        if (studentInfoOpt.isPresent()) {
            StudentInfo studentInfo = studentInfoOpt.get();
            response.setAddress(studentInfo.getAddress());
            response.setAverageScore(studentInfo.getAverageScore());
            response.setDateOfBirth(studentInfo.getDateOfBirth());
        }

        return response;
    }
}
