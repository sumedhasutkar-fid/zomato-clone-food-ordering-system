package com.zomato.app;

import com.zomato.app.dto.LoginRequest;
import com.zomato.app.dto.LoginResponse;
import com.zomato.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthIntegrationTests {

    @Autowired
    private UserService userService;

    @Test
    void demoUserCanLoginAndReceiveJwt() {
        LoginRequest request = new LoginRequest();
        request.setEmail("demo@zomato.com");
        request.setPassword("demo123");

        LoginResponse response = userService.login(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getRefreshToken()).isNotBlank();
        assertThat(response.getRole()).isEqualTo("CUSTOMER");
    }
}
