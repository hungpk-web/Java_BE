package com.example.Student_BE.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Column;
import java.time.LocalDateTime;


@Entity(immutable = true)
@Data
@AllArgsConstructor
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


}

