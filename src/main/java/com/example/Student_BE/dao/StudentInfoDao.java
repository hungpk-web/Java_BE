package com.example.Student_BE.dao;

import com.example.Student_BE.entity.StudentInfo;
import com.example.Student_BE.entity.StudentInfoWithStudent;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.Result;

import java.util.List;
import java.util.Optional;

/**
 * Doma2 DAO cho StudentInfo entity
 * Cung cấp các method CRUD cơ bản và custom queries
 */
@Dao
public interface StudentInfoDao {

    /**
     * Tìm StudentInfo theo student_id
     */
    @Select
    @Sql("SELECT * FROM student_info WHERE student_id = /*studentId*/1")
    Optional<StudentInfo> findByStudentId(Integer studentId);

    /**
     * Thêm student info mới
     */
    @Insert
    Result<StudentInfo> insert(StudentInfo studentInfo);

    /**
     * Cập nhật student info
     */
    @Update
    Result<StudentInfo> update(StudentInfo studentInfo);

    /**
     * Xóa student info
     */
    @Delete
    Result<StudentInfo> delete(StudentInfo studentInfo);
}

