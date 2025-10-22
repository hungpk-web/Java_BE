package com.example.Student_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // <-- THÊM DÒNG NÀY
import org.springframework.context.annotation.FilterType; // <-- THÊM DÒNG NÀY
import org.springframework.context.annotation.Lazy;


@Lazy

@SpringBootApplication
public class StudentBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentBeApplication.class, args);
	}

}
