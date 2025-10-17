package com.example.Student_BE.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Column;

@Data
@AllArgsConstructor
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

    public User(String userName, String password) {
        this.userId = null;
        this.userName = userName;
        this.password = password;
    }


}
