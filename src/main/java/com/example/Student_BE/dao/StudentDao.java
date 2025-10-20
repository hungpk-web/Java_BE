package com.example.Student_BE.dao;

import com.example.Student_BE.entity.Student;
import com.example.Student_BE.entity.StudentWithInfo;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.Result;

import java.util.List;

@Dao
public interface StudentDao {

    /**
     * Lấy tất cả students
     */
    @Select
    @Sql("SELECT * FROM student ORDER BY student_id")
    List<Student> selectAll();

    /**
     * Tìm student theo ID
     */
    @Select
    @Sql("SELECT * FROM student WHERE student_id = /*id*/1")
    Student selectById(Integer id);


    /**
     * Kiểm tra student code có tồn tại
     */
    @Select
    @Sql("SELECT COUNT(*) FROM student WHERE student_code = /*code*/'ST001'")
    boolean existsByStudentCode(String code);

    /**
     * Tìm kiếm student theo tên (LIKE query)
     */
    @Select
    @Sql("SELECT * FROM student WHERE student_name LIKE /*name*/'%John%'")
    List<Student> findByStudentNameContaining(String name);

    /**
     * Truy xuat du lieu co limit tranh OOM
     */
    @Select
    @Sql("SELECT * FROM student ORDER BY student_id LIMIT /*limit*/100 OFFSET /*offset*/0")
    List<Student> selectAllWithPagination(int limit, int offset);
    /**
     * Thêm student mới
     */
    @Insert
    Result<Student> insert(Student student);

    /**
     * Cập nhật student
     */
    @Update
    Result<Student> update(Student student);

    /**
     * Xóa student
     */
    @Delete
    Result<Student> delete(Student student);
}

