package com.example.Student_BE.config;

import com.example.Student_BE.dao.StudentDao;
import com.example.Student_BE.dao.StudentDaoImpl;
import com.example.Student_BE.dao.StudentInfoDao;
import com.example.Student_BE.dao.StudentInfoDaoImpl;
import com.example.Student_BE.dao.UserDao;
import com.example.Student_BE.dao.UserDaoImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Doma2 Configuration
 * Sử dụng properties từ application.properties để cấu hình DataSource
 */
@Configuration
@EnableTransactionManagement
public class DomaConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.connection-timeout:30000}")
    private long connectionTimeout;

    @Value("${spring.datasource.hikari.initialization-fail-timeout:120000}")
    private long initializationFailTimeout;

    @Value("${spring.datasource.hikari.maximum-pool-size:5}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle:1}")
    private int minimumIdle;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        // Cấu hình kết nối từ properties
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);

        // Cấu hình connection pool
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setInitializationFailTimeout(initializationFailTimeout);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);

        // Cấu hình retry và validation
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setValidationTimeout(5000);

        return new HikariDataSource(hikariConfig);
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
