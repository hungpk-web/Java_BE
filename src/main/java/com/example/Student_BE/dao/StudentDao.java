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
import java.util.Optional;

/**
 * Doma2 DAO cho Student entity
 * Cung cấp các method CRUD cơ bản và custom queries
 */
@Dao
public interface StudentDao {

    /**
     * Lấy tất cả students
     */
    @Select
    List<Student> selectAll();

    /**
     * Tìm student theo ID
     */
    @Select
    Student selectById(Integer id);

    /**
     * Tìm student theo student code
     */
    @Select
    @Sql("SELECT * FROM student WHERE student_code = /*code*/'ST001'")
    Optional<Student> findByStudentCode(String code);

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
     * Lấy danh sách student với thông tin chi tiết
     */
    @Select
    @Sql("""
        SELECT s.*, si.info_id, si.address, si.average_score, si.date_of_birth
        FROM student s 
        LEFT JOIN student_info si ON s.student_id = si.student_id
        """)
    List<StudentWithInfo> findAllWithStudentInfo();

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

