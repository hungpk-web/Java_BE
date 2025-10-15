package com.example.Student_BE.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Column;

/**
 * Doma2 Entity class cho bảng user
 * Tương ứng với schema: user_id, user_name, password
 */
@Entity(immutable = true)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private final Integer userId;

    @Column(name = "user_name")
    private final String userName;

    @Column(name = "password")
    private final String password;

    public User(Integer userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Constructor cho việc tạo user mới (không cần userId vì tự động tăng)
     */
    public User(String userName, String password) {
        this.userId = null;
        this.userName = userName;
        this.password = password;
    }

    // Getters
    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
