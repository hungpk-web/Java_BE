package com.example.Student_BE.config;

import com.example.Student_BE.entity.User;
import com.example.Student_BE.entity.Student;
import com.example.Student_BE.entity.StudentInfo;
import com.example.Student_BE.dao.UserDao;
import com.example.Student_BE.dao.StudentDao;
import com.example.Student_BE.dao.StudentInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Class để khởi tạo dữ liệu mẫu
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo user mẫu nếu chưa có
        if (userDao.selectAll().isEmpty()) {
            createSampleUsers();
        }

        // Tạo student mẫu nếu chưa có
        if (studentDao.selectAll().isEmpty()) {
            createSampleStudents();
        }
    }

    private void createSampleUsers() {
        User admin = new User("admin", passwordEncoder.encode("123456"));
        userDao.insert(admin);

        User user = new User("user", passwordEncoder.encode("123456"));
        userDao.insert(user);
    }

    private void createSampleStudents() {
        // Student 1
        Student student1 = new Student(null, "Nguyễn Văn A", "SV001");
        studentDao.insert(student1);

        StudentInfo info1 = new StudentInfo(null, student1.getStudentId(),
                "123 Đường ABC, Quận 1, TP.HCM", 8.5, LocalDateTime.of(2000, 1, 15, 0, 0));
        studentInfoDao.insert(info1);

        // Student 2
        Student student2 = new Student(null, "Trần Thị B", "SV002");
        studentDao.insert(student2);

        StudentInfo info2 = new StudentInfo(null, student2.getStudentId(),
                "456 Đường XYZ, Quận 2, TP.HCM", 9.0, LocalDateTime.of(2001, 3, 20, 0, 0));
        studentInfoDao.insert(info2);

        // Student 3
        Student student3 = new Student(null, "Lê Văn C", "SV003");
        studentDao.insert(student3);

        StudentInfo info3 = new StudentInfo(null, student3.getStudentId(),
                "789 Đường DEF, Quận 3, TP.HCM", 7.8, LocalDateTime.of(1999, 7, 10, 0, 0));
        studentInfoDao.insert(info3);
    }
}
