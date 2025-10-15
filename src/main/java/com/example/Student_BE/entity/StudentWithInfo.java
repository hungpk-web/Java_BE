package com.example.Student_BE.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Column;
import java.time.LocalDateTime;

/**
 * Doma2 Entity cho Student với StudentInfo
 * Sử dụng cho JOIN queries
 */
@Entity(immutable = true)
public class StudentWithInfo {

    @Column(name = "student_id")
    private final Integer studentId;

    @Column(name = "student_name")
    private final String studentName;

    @Column(name = "student_code")
    private final String studentCode;

    @Column(name = "info_id")
    private final Integer infoId;

    @Column(name = "address")
    private final String address;

    @Column(name = "average_score")
    private final Double averageScore;

    @Column(name = "date_of_birth")
    private final LocalDateTime dateOfBirth;

    public StudentWithInfo(Integer studentId, String studentName, String studentCode,
                           Integer infoId, String address, Double averageScore, LocalDateTime dateOfBirth) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.infoId = infoId;
        this.address = address;
        this.averageScore = averageScore;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters
    public Integer getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentCode() { return studentCode; }
    public Integer getInfoId() { return infoId; }
    public String getAddress() { return address; }
    public Double getAverageScore() { return averageScore; }
    public LocalDateTime getDateOfBirth() { return dateOfBirth; }
}

