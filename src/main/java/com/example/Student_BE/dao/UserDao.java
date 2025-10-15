package com.example.Student_BE.dao;

import com.example.Student_BE.entity.User;
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
 * Doma2 DAO cho User entity
 * Cung cấp các method CRUD cơ bản và custom queries
 */
@Dao
public interface UserDao {

    /**
     * Lấy tất cả users
     */
    @Select
    List<User> selectAll();

    /**
     * Tìm user theo ID
     */
    @Select
    User selectById(Integer id);

    /**
     * Tìm user theo username
     */
    @Select
    @Sql("SELECT * FROM user WHERE user_name = /*userName*/'admin'")
    Optional<User> findByUserName(String userName);

    /**
     * Kiểm tra user có tồn tại theo username
     */
    @Select
    @Sql("SELECT COUNT(*) FROM user WHERE user_name = /*userName*/'admin'")
    boolean existsByUserName(String userName);

    /**
     * Thêm user mới
     */
    @Insert
    Result<User> insert(User user);

    /**
     * Cập nhật user
     */
    @Update
    Result<User> update(User user);

    /**
     * Xóa user
     */
    @Delete
    Result<User> delete(User user);
}

