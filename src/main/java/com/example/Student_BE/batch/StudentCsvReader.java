package com.example.Student_BE.batch;

import com.example.Student_BE.entity.Student;
import com.example.Student_BE.dao.StudentDao;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * Reader để đọc dữ liệu Student từ database
 */
@Component
public class StudentCsvReader implements ItemReader<Student> {

    @Autowired
    private StudentDao studentDao;
    private Iterator<Student> studentIterator;
    private int offset = 0;
    private int limit = 100;

    @Override
    public Student read() throws Exception {
        if (studentIterator == null || !studentIterator.hasNext()) {
            List<Student> students = studentDao.selectAllWithPagination(limit, offset);
            if (students.isEmpty()) {
                return null;
            }

            studentIterator = students.iterator();
            offset += limit;
        }
        return studentIterator.next();
    }
}
