package com.example.Student_BE.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Column;
import java.time.LocalDateTime;

/**
 * Doma2 Entity cho StudentInfo với Student
 * Sử dụng cho JOIN queries
 */
@Entity(immutable = true)
public class StudentInfoWithStudent {

    @Column(name = "info_id")
    private final Integer infoId;

    @Column(name = "student_id")
    private final Integer studentId;

    @Column(name = "address")
    private final String address;

    @Column(name = "average_score")
    private final Double averageScore;

    @Column(name = "date_of_birth")
    private final LocalDateTime dateOfBirth;

    @Column(name = "student_name")
    private final String studentName;

    @Column(name = "student_code")
    private final String studentCode;

    public StudentInfoWithStudent(Integer infoId, Integer studentId, String address,
                                  Double averageScore, LocalDateTime dateOfBirth,
                                  String studentName, String studentCode) {
        this.infoId = infoId;
        this.studentId = studentId;
        this.address = address;
        this.averageScore = averageScore;
        this.dateOfBirth = dateOfBirth;
        this.studentName = studentName;
        this.studentCode = studentCode;
    }

    // Getters
    public Integer getInfoId() { return infoId; }
    public Integer getStudentId() { return studentId; }
    public String getAddress() { return address; }
    public Double getAverageScore() { return averageScore; }
    public LocalDateTime getDateOfBirth() { return dateOfBirth; }
    public String getStudentName() { return studentName; }
    public String getStudentCode() { return studentCode; }
}

