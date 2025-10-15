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
     * Lấy tất cả student info
     */
    @Select
    List<StudentInfo> selectAll();

    /**
     * Tìm student info theo ID
     */
    @Select
    StudentInfo selectById(Integer id);

    /**
     * Tìm StudentInfo theo student_id
     */
    @Select
    @Sql("SELECT * FROM student_info WHERE student_id = /*studentId*/1")
    Optional<StudentInfo> findByStudentId(Integer studentId);

    /**
     * Lấy danh sách StudentInfo với điểm trung bình >= threshold
     */
    @Select
    @Sql("SELECT * FROM student_info WHERE average_score >= /*threshold*/8.0")
    List<StudentInfo> findByAverageScoreGreaterThanEqual(Double threshold);

    /**
     * Lấy danh sách StudentInfo với điểm trung bình trong khoảng
     */
    @Select
    @Sql("SELECT * FROM student_info WHERE average_score BETWEEN /*minScore*/7.0 AND /*maxScore*/10.0")
    List<StudentInfo> findByAverageScoreBetween(Double minScore, Double maxScore);

    /**
     * Lấy tất cả StudentInfo với Student được load
     */
    @Select
    @Sql("""
        SELECT si.*, s.student_name, s.student_code
        FROM student_info si 
        LEFT JOIN student s ON si.student_id = s.student_id
        """)
    List<StudentInfoWithStudent> findAllWithStudent();

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

