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


@Dao
public interface UserDao {

    /**
     * Lấy tất cả users
     */
    @Select
    @Sql("SELECT * FROM user ORDER BY user_id")
    List<User> selectAll();

    /**
     * Tìm user theo username
     */
    @Select
    @Sql("SELECT * FROM user WHERE user_name = /*userName*/'admin'")
    Optional<User> findByUserName(String userName);

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

