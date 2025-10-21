package com.example.Student_BE.batch;

import com.example.Student_BE.entity.Student;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class StudentCsvReader {

    @Autowired
    private DataSource dataSource;
    @Bean
    public JdbcCursorItemReader<Student> studentJdbcCursorReader() {
        JdbcCursorItemReader<Student> reader = new JdbcCursorItemReader<>();


        reader.setDataSource(dataSource);

        reader.setSql("SELECT s.*, si.info_id, si.address, si.average_score, si.date_of_birth " +
                "FROM student s " +
                "LEFT JOIN student_info si ON s.student_id = si.student_id " +
                "ORDER BY student_id");
                reader.setRowMapper(new BeanPropertyRowMapper<>(Student.class));


        reader.setFetchSize(100);

        return reader;
    }
}
