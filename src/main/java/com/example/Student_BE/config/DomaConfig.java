package com.example.Student_BE.config;

import com.example.Student_BE.dao.StudentDao;
import com.example.Student_BE.dao.StudentDaoImpl;
import com.example.Student_BE.dao.StudentInfoDao;
import com.example.Student_BE.dao.StudentInfoDaoImpl;
import com.example.Student_BE.dao.UserDao;
import com.example.Student_BE.dao.UserDaoImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Doma2 Configuration
 */
@Configuration
@EnableTransactionManagement
public class DomaConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/student_db");
        dataSource.setUsername("student");
        dataSource.setPassword("Student@123");
        return dataSource;
    }

    @Bean
    public Config config(DataSource dataSource) {
        return new Config() {
            @Override
            public Dialect getDialect() {
                return new MysqlDialect();
            }

            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        };
    }

    /**
     * Đăng ký StudentDao implementation như Spring bean
     */
    @Bean
    public StudentDao studentDao(Config config) {
        return new StudentDaoImpl(config);
    }

    /**
     * Đăng ký StudentInfoDao implementation như Spring bean
     */
    @Bean
    public StudentInfoDao studentInfoDao(Config config) {
        return new StudentInfoDaoImpl(config);
    }

    /**
     * Đăng ký UserDao implementation như Spring bean
     */
    @Bean
    public UserDao userDao(Config config) {
        return new UserDaoImpl(config);
    }
}
