package com.example.Student_BE.service;

import com.example.Student_BE.dto.LoginRequest;
import com.example.Student_BE.dto.LoginResponse;
import com.example.Student_BE.dto.RegisterRequest;
import com.example.Student_BE.entity.User;
import com.example.Student_BE.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service xử lý authentication
 */
@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Đăng nhập user
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // Tìm user theo username
        Optional<User> userOpt = userDao.findByUserName(loginRequest.getUserName());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Username không tồn tại");
        }

        User user = userOpt.get();

        // Kiểm tra password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password không đúng");
        }

        // Tạo JWT token: kèm userId, loginTime, exp
        long now = System.currentTimeMillis();
        String token = jwtService.generateToken(user.getUserId(), user.getUserName());

        // Format thời gian dạng yyyy-MM-dd HH:mm:ss
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        java.time.ZoneId zone = java.time.ZoneId.systemDefault();
        String loginAt = java.time.Instant.ofEpochMilli(now).atZone(zone).format(fmt);
        String expiresAt = java.time.Instant.ofEpochMilli(now + expiration).atZone(zone).format(fmt);

        return new LoginResponse(token, "Bearer", loginAt, expiresAt, user.getUserId(), user.getUserName());
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token, String username) {
        return jwtService.validateToken(token, username);
    }

    /**
     * Lấy username từ token
     */
    public String getUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    /**
     * Đăng ký user mới
     */
    public void register(RegisterRequest registerRequest) {
        // Kiểm tra username đã tồn tại chưa
        Optional<User> userOpt = userDao.findByUserName(registerRequest.getUserName());
        if (userOpt.isPresent()) {
            throw new RuntimeException("Username đã tồn tại");
        }

        // Kiểm tra password và confirmPassword có trùng khớp không
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RuntimeException("Password không trùng khớp");
        }

        // Mã hóa password trước khi lưu
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Tạo user mới (userId sẽ tự động tăng)
        User user = new User(registerRequest.getUserName(), encodedPassword);

        // Lưu user vào database
        userDao.insert(user);
    }
}
