package com.example.Student_BE.service;

import com.example.Student_BE.entity.Student;
import com.example.Student_BE.entity.StudentInfo;
import com.example.Student_BE.dao.StudentDao;
import com.example.Student_BE.dao.StudentInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service để export dữ liệu ra CSV
 */
@Service
public class CsvExportService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Value("${app.csv.export.path}")
    private String exportPath;

    /**
     * Export tất cả students ra file CSV
     */
    public String exportStudentsToCsv() throws IOException {
        // Lấy danh sách students bằng Doma DAO
        List<Student> students = studentDao.selectAll();

        // Tạo tên file với timestamp
        String fileName = "students_export_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        String filePath = exportPath + fileName;

        // Tạo thư mục nếu chưa có
        java.io.File exportDir = new java.io.File(exportPath);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        // Ghi file CSV
        try (FileWriter writer = new FileWriter(filePath)) {
            // Ghi header
            writer.append("Student ID,Student Name,Student Code,Address,Average Score,Date of Birth\n");

            // Ghi dữ liệu
            for (Student student : students) {
                StudentInfo studentInfo = studentInfoDao.findByStudentId(student.getStudentId()).orElse(null);

                writer.append(String.valueOf(student.getStudentId())).append(",");
                writer.append("\"").append(student.getStudentName()).append("\"").append(",");
                writer.append("\"").append(student.getStudentCode()).append("\"").append(",");

                if (studentInfo != null) {
                    writer.append("\"").append(studentInfo.getAddress()).append("\"").append(",");
                    writer.append(String.valueOf(studentInfo.getAverageScore())).append(",");
                    writer.append("\"").append(studentInfo.getDateOfBirth().toString()).append("\"");
                } else {
                    writer.append("\"\",\"\",\"\"");
                }

                writer.append("\n");
            }
        }

        return filePath;
    }
}
