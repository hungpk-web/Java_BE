package com.example.Student_BE.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Column;

/**
 * Doma2 Entity class cho bảng student
 * Tương ứng với schema: student_id, student_name, student_code
 */
@Entity(immutable = true)
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private final Integer studentId;

    @Column(name = "student_name")
    private final String studentName;

    @Column(name = "student_code")
    private final String studentCode;

    public Student(Integer studentId, String studentName, String studentCode) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
    }

    // Getters
    public Integer getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }
}
