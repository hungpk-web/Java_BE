package com.example.Student_BE.service;

import com.example.Student_BE.dto.LoginRequestDto;
import com.example.Student_BE.dto.LoginResponseDto;
import com.example.Student_BE.dto.RegisterRequestDto;
import com.example.Student_BE.entity.User;
import com.example.Student_BE.dao.UserDao;
import com.example.Student_BE.utils.getToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.example.Student_BE.utils.DateTimeUtils;
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
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // Tìm user theo username
        Optional<User> userOpt = userDao.findByUserName(loginRequestDto.getUserName());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Username không tồn tại");
        }

        User user = userOpt.get();

        // Kiểm tra password
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password không đúng");
        }

        // Tạo JWT token: kèm userId, loginTime, exp
        long now = System.currentTimeMillis();
        String token = jwtService.generateToken(user.getUserId(), user.getUserName());

        // Format thời gian dạng yyyy-MM-dd HH:mm:ss
        String loginAt = DateTimeUtils.formatEpochMillis(now);
        String expiresAt = DateTimeUtils.formatEpochMillis(now + expiration);
        getToken.token=token;

        return new LoginResponseDto(token, "Bearer", loginAt, expiresAt, user.getUserId(), user.getUserName());
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
    public void register(RegisterRequestDto registerRequest) {
        Optional<User> userOpt = userDao.findByUserName(registerRequest.getUserName());
        if (userOpt.isPresent()) {
            throw new RuntimeException("Username đã tồn tại");
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RuntimeException("Password không trùng khớp");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = new User(registerRequest.getUserName(), encodedPassword);

        userDao.insert(user);
    }
}
