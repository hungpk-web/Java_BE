package com.example.Student_BE.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Column;


@Entity(immutable = true)
@Table(name = "student")
@Data
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private final Integer studentId;

    @Column(name = "student_name")
    private final String studentName;

    @Column(name = "student_code")
    private final String studentCode;


}
