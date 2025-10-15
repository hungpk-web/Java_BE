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

    @Override
    public Student read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (studentIterator == null) {
            List<Student> students = studentDao.selectAll();
            studentIterator = students.iterator();
        }

        if (studentIterator.hasNext()) {
            return studentIterator.next();
        } else {
            return null; // Kết thúc đọc
        }
    }
}
