package com.example.Student_BE.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Column;
import java.time.LocalDateTime;

/**
 * Doma2 Entity class cho bảng student_info
 * Tương ứng với schema: info_id, student_id, address, average_score, date_of_birth
 */
@Entity(immutable = true)
@Table(name = "student_info")
@Data
@AllArgsConstructor
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}
