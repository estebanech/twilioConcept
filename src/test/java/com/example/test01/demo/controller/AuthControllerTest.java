package com.example.test01.demo.controller;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.httpModel.CustomResponse;
import com.example.test01.demo.httpModel.auth.LogInResponse;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthControllerTest {

    @MockBean
    private UserService service;

    @Autowired
    private AuthController controller;

    @Autowired
    private PasswordEncoder encoder;

    private UserIn validUser;

    @BeforeEach
    void setup(){
        validUser = UserIn.builder()
                .id(0L)
                .email("test@test.com")
                .phone("1975482967")
                .countryCode("15")
                .password(encoder.encode("123456"))
                .authyId(0)
                .build();
    }


    @Test
    void signUp() {
        /*final UserIn validUser = UserIn.builder()
                .id(0L)
                .email("test@test.com")
                .phone("1975482967")
                .countryCode("15")
                .password(encoder.encode("123456"))
                .authyId(0)
                .build();*/
        final SignUpRequest request = new SignUpRequest();
        request.setEmail("test@test");
        request.setPhone("1975482967");
        request.setCountryCode("15");
        request.setPassword("123456");
        when(service.createUser(request)).thenReturn(Optional.of(validUser));
        final ResponseEntity<CustomResponse<LogInResponse>> controllerResponse = controller.signUp(request);
        assertEquals(0,controllerResponse.getBody().getData().getAuthyId());
    }

    @Test
    void logIn() {
    }

    @Test
    void verify() {
    }

    @Test
    void refresh() {
    }
}