package com.zomato.app.util;

import com.zomato.app.dto.LoginRequest;
import com.zomato.app.dto.RegisterRequest;
import com.zomato.app.exception.InvalidRequestException;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Request body is required");
        }

        validateRequired(request.getName(), "Name");
        validateEmail(request.getEmail());
        validateRequired(request.getPassword(), "Password");
        validateRequired(request.getMobile(), "Mobile");
        validateRequired(request.getAddress(), "Address");

        if (request.getPassword().trim().length() < 6) {
            throw new InvalidRequestException("Password must be at least 6 characters");
        }
    }

    public void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Request body is required");
        }

        validateEmail(request.getEmail());
        validateRequired(request.getPassword(), "Password");
    }

    private void validateEmail(String email) {
        validateRequired(email, "Email");

        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidRequestException("Please enter a valid email");
        }
    }

    private void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidRequestException(fieldName + " is required");
        }
    }
}
