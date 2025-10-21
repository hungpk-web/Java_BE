package com.example.Student_BE.service;

import com.example.Student_BE.dao.StudentDao;
import com.example.Student_BE.dao.StudentInfoDao;
import com.example.Student_BE.dto.StudentRequestDto;
import com.example.Student_BE.dto.StudentResponseDto;
import com.example.Student_BE.entity.Student;
import com.example.Student_BE.entity.StudentInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seasar.doma.jdbc.Result;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private StudentInfoDao studentInfoDao;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private StudentInfo testStudentInfo;
    private StudentRequestDto StudentRequestDto;
    private StudentResponseDto StudentResponseDto;

    @BeforeEach
    void initData() {

        testStudent = new Student(1, "Nguyen Van A", "ST001");
        testStudentInfo = new StudentInfo(1, 1, "123 ABC Street", 8.5, LocalDateTime.now());


        StudentRequestDto = new StudentRequestDto();
        StudentRequestDto.setStudentName("Nguyen Van A");
        StudentRequestDto.setStudentCode("ST001");
        StudentRequestDto.setAddress("123 ABC Street");
        StudentRequestDto.setAverageScore(8.5);
        StudentRequestDto.setDateOfBirth(LocalDateTime.now());


        StudentResponseDto = new StudentResponseDto();
        StudentResponseDto.setStudentId(1);
        StudentResponseDto.setStudentName("Nguyen Van A");
        StudentResponseDto.setStudentCode("ST001");
        StudentResponseDto.setAddress("123 ABC Street");
        StudentResponseDto.setAverageScore(8.5);
        StudentResponseDto.setDateOfBirth(LocalDateTime.now());
    }

    @Test
    void getAllStudents_success() {
        // GIVEN
        List<Student> students = Arrays.asList(testStudent);
        when(studentDao.selectAll()).thenReturn(students);
        when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(testStudentInfo));

        // WHEN
        List<StudentResponseDto> result = studentService.getAllStudents();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Nguyen Van A", result.get(0).getStudentName());
        assertEquals("ST001", result.get(0).getStudentCode());

        verify(studentDao).selectAll();
        verify(studentInfoDao).findByStudentId(1);
    }

    @Test
    void getStudentById_success() {
        // GIVEN
        when(studentDao.selectById(1)).thenReturn(testStudent);
        when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(testStudentInfo));

        // WHEN
        StudentResponseDto result = studentService.getStudentById(1);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        assertEquals("Nguyen Van A", result.getStudentName());
        assertEquals("ST001", result.getStudentCode());

        verify(studentDao).selectById(1);
        verify(studentInfoDao).findByStudentId(1);
    }

    @Test
    void getStudentById_notFound_throwsException() {
        // GIVEN
        when(studentDao.selectById(999)).thenReturn(null);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.getStudentById(999);
        });

        assertEquals("Student không tồn tại với ID: 999", exception.getMessage());
        verify(studentDao).selectById(999);
        verify(studentInfoDao, never()).findByStudentId(anyInt());
    }
    @Test
    void createStudent_success() {
        // GIVEN
        when(studentDao.existsByStudentCode("ST001")).thenReturn(false);

        Result<Student> studentResult = mock(Result.class);
        when(studentResult.getEntity()).thenReturn(testStudent);
        when(studentDao.insert(any(Student.class))).thenReturn(studentResult);

        Result<StudentInfo> studentInfoResult = mock(Result.class);
        when(studentInfoResult.getEntity()).thenReturn(testStudentInfo);
        when(studentInfoDao.insert(any(StudentInfo.class))).thenReturn(studentInfoResult);

        // Mock cho convertToResponse method
        when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(testStudentInfo));

        // WHEN
        StudentResponseDto result = studentService.createStudent(StudentRequestDto);

        // THEN
        assertNotNull(result);
        assertEquals("Nguyen Van A", result.getStudentName());
        assertEquals("ST001", result.getStudentCode());
        assertEquals("123 ABC Street", result.getAddress());
        assertEquals(8.5, result.getAverageScore());

        verify(studentDao).existsByStudentCode("ST001");
        verify(studentDao).insert(any(Student.class));
        verify(studentInfoDao).insert(any(StudentInfo.class));
        verify(studentInfoDao).findByStudentId(1);
    }

    @Test
    void createStudent_duplicateStudentCode_throwsException() {
        // GIVEN
        when(studentDao.existsByStudentCode("ST001")).thenReturn(true);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.createStudent(StudentRequestDto);
        });

        assertEquals("Mã sinh viên đã tồn tại: ST001", exception.getMessage());
        verify(studentDao).existsByStudentCode("ST001");
        verify(studentDao, never()).insert(any(Student.class));
        verify(studentInfoDao, never()).insert(any(StudentInfo.class));
    }

    @Test
    void updateStudent_success() {
        // GIVEN
        when(studentDao.selectById(1)).thenReturn(testStudent);
        Result<Student> studentResult = mock(Result.class);
        when(studentResult.getEntity()).thenReturn(testStudent);
        when(studentDao.update(any(Student.class))).thenReturn(studentResult);
        when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(testStudentInfo));
        Result<StudentInfo> studentInfoResult = mock(Result.class);
        when(studentInfoResult.getEntity()).thenReturn(testStudentInfo);
        when(studentInfoDao.update(any(StudentInfo.class))).thenReturn(studentInfoResult);
        // WHEN
        StudentResponseDto result = studentService.updateStudent(1, StudentRequestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        assertEquals("Nguyen Van A", result.getStudentName());
        assertEquals("ST001", result.getStudentCode());
        assertEquals("123 ABC Street", result.getAddress());
        assertEquals(8.5, result.getAverageScore());

        verify(studentDao).selectById(1);
        verify(studentDao, never()).existsByStudentCode(anyString());
        verify(studentDao).update(any(Student.class));
        verify(studentInfoDao,times(2)).findByStudentId(1);
        verify(studentInfoDao).update(any(StudentInfo.class));
    }

    @Test
    void updateStudent_notFound_throwsException() {
        // GIVEN
        when(studentDao.selectById(999)).thenReturn(null);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(999, StudentRequestDto);
        });

        assertEquals("Student không tồn tại với ID: 999", exception.getMessage());
        verify(studentDao).selectById(999);
        verify(studentDao, never()).update(any(Student.class));
        verify(studentInfoDao, never()).update(any(StudentInfo.class));
    }

    @Test
    void deleteStudent_success() {
        // GIVEN
        when(studentDao.selectById(1)).thenReturn(testStudent);
        when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(testStudentInfo));

        Result<StudentInfo> studentInfoResult = mock(Result.class);
        when(studentInfoResult.getEntity()).thenReturn(testStudentInfo);
        when(studentInfoDao.delete(any(StudentInfo.class))).thenReturn(studentInfoResult);

        Result<Student> studentResult = mock(Result.class);
        when(studentResult.getEntity()).thenReturn(testStudent);
        when(studentDao.delete(any(Student.class))).thenReturn(studentResult);

        // WHEN
        studentService.deleteStudent(1);

        // THEN
        verify(studentDao).selectById(1);
        verify(studentInfoDao).findByStudentId(1);
        verify(studentInfoDao).delete(any(StudentInfo.class));
        verify(studentDao).delete(any(Student.class));
    }

    @Test
    void deleteStudent_notFound_throwsException() {
        // GIVEN
        when(studentDao.selectById(999)).thenReturn(null);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.deleteStudent(999);
        });

        assertEquals("Student không tồn tại với ID: 999", exception.getMessage());
        verify(studentDao).selectById(999);
        verify(studentInfoDao, never()).findByStudentId(anyInt());
        verify(studentInfoDao, never()).delete(any(StudentInfo.class));
        verify(studentDao, never()).delete(any(Student.class));
    }

}
