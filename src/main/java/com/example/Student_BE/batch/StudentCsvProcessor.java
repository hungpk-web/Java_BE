package com.example.Student_BE.batch;

import com.example.Student_BE.entity.Student;
import com.example.Student_BE.entity.StudentInfo;
import com.example.Student_BE.dao.StudentInfoDao;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Processor để xử lý dữ liệu Student trước khi ghi vào CSV
 * Chuyển đổi từ Student entity sang String array cho CSV
 */
@Component
public class StudentCsvProcessor implements ItemProcessor<Student, String[]> {

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Override
    public String[] process(Student student) throws Exception {
        // Chuyển đổi Student entity thành String array cho CSV
        String[] csvRow = new String[6];

        csvRow[0] = String.valueOf(student.getStudentId());
        csvRow[1] = student.getStudentName();
        csvRow[2] = student.getStudentCode();

        // Lấy thông tin từ StudentInfo nếu có
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(student.getStudentId());
        if (studentInfoOpt.isPresent()) {
            StudentInfo studentInfo = studentInfoOpt.get();
            csvRow[3] = studentInfo.getAddress();
            csvRow[4] = String.valueOf(studentInfo.getAverageScore());
            csvRow[5] = studentInfo.getDateOfBirth().toString();
        } else {
            csvRow[3] = "N/A";
            csvRow[4] = "N/A";
            csvRow[5] = "N/A";
        }

        return csvRow;
    }
}
