package com.zomato.app.service;

import com.zomato.app.dto.LoginRequest;
import com.zomato.app.dto.LoginResponse;
import com.zomato.app.dto.RefreshTokenRequest;
import com.zomato.app.dto.RegisterRequest;
import com.zomato.app.entity.RefreshToken;
import com.zomato.app.entity.User;
import com.zomato.app.exception.DuplicateResourceException;
import com.zomato.app.exception.InvalidCredentialsException;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.RefreshTokenRepository;
import com.zomato.app.repository.UserRepository;
import com.zomato.app.security.JwtUtil;
import com.zomato.app.util.AppConstants;
import com.zomato.app.util.ValidationUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder encoder;
    private final ValidationUtil validationUtil;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository repository,
                       RefreshTokenRepository refreshTokenRepository,
                       BCryptPasswordEncoder encoder,
                       ValidationUtil validationUtil,
                       JwtUtil jwtUtil) {

        this.repository = repository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.encoder = encoder;
        this.validationUtil = validationUtil;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String register(RegisterRequest request) {
        validationUtil.validateRegisterRequest(request);

        if (repository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setMobile(request.getMobile());
        user.setAddress(request.getAddress());
        user.setRole(AppConstants.CUSTOMER_ROLE);

        repository.save(user);

        return AppConstants.REGISTER_SUCCESS;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        validationUtil.validateLoginRequest(request);

        User user = repository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return createLoginResponse(user, AppConstants.LOGIN_SUCCESS);
    }

    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        if (request == null || request.getRefreshToken() == null || request.getRefreshToken().trim().isEmpty()) {
            throw new InvalidRequestException("Refresh token is required");
        }

        RefreshToken savedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        if (savedToken.getExpiresAt().isBefore(LocalDateTime.now()) || !jwtUtil.isTokenValid(savedToken.getToken())) {
            throw new InvalidCredentialsException("Refresh token expired");
        }

        User user = repository.findByEmail(savedToken.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        return createLoginResponse(user, "Token refreshed successfully");
    }

    private LoginResponse createLoginResponse(User user, String message) {
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getRole());

        refreshTokenRepository.deleteByEmail(user.getEmail());
        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getEmail(), LocalDateTime.now().plusDays(7)));

        return new LoginResponse(accessToken, refreshToken, user.getRole(), user.getName(), message);
    }
}
