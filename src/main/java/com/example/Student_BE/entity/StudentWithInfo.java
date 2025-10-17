package com.example.Student_BE.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Column;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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


}

