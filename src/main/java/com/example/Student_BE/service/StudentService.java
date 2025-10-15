package com.example.Student_BE.service;

import com.example.Student_BE.dto.StudentRequest;
import com.example.Student_BE.dto.StudentResponse;
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
    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentDao.selectAll();
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy student theo ID
     */
    public StudentResponse getStudentById(Integer studentId) {
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }
        return convertToResponse(student);
    }

    /**
     * Tạo student mới
     */
    public StudentResponse createStudent(StudentRequest studentRequest) {
        // Kiểm tra student code đã tồn tại chưa
        if (studentDao.existsByStudentCode(studentRequest.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại: " + studentRequest.getStudentCode());
        }

        // Tạo Student entity
        Student student = new Student(null, studentRequest.getStudentName(), studentRequest.getStudentCode());

        // Lưu Student và lấy entity với ID đã được tạo
        Student savedStudent = studentDao.insert(student).getEntity();

        // Tạo StudentInfo
        StudentInfo studentInfo = new StudentInfo(null, savedStudent.getStudentId(),
                studentRequest.getAddress(), studentRequest.getAverageScore(), studentRequest.getDateOfBirth());

        // Lưu StudentInfo
        studentInfoDao.insert(studentInfo).getEntity();

        return convertToResponse(savedStudent);
    }

    /**
     * Cập nhật student
     */
    public StudentResponse updateStudent(Integer studentId, StudentRequest studentRequest) {
        // Tìm student
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }

        // Kiểm tra student code có thay đổi và đã tồn tại chưa
        if (!student.getStudentCode().equals(studentRequest.getStudentCode())
                && studentDao.existsByStudentCode(studentRequest.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại: " + studentRequest.getStudentCode());
        }

        // Cập nhật thông tin Student
        Student updatedStudent = new Student(studentId, studentRequest.getStudentName(), studentRequest.getStudentCode());
        studentDao.update(updatedStudent).getEntity();

        // Cập nhật StudentInfo
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(studentId);
        if (studentInfoOpt.isPresent()) {
            StudentInfo studentInfo = studentInfoOpt.get();
            StudentInfo updatedStudentInfo = new StudentInfo(studentInfo.getInfoId(), studentId,
                    studentRequest.getAddress(), studentRequest.getAverageScore(), studentRequest.getDateOfBirth());
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
    public List<StudentResponse> searchStudentsByName(String name) {
        List<Student> students = studentDao.findByStudentNameContaining("%" + name + "%");
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Student entity to StudentResponse
     */
    private StudentResponse convertToResponse(Student student) {
        StudentResponse response = new StudentResponse();
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
