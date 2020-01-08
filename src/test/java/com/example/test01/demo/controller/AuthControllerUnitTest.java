package com.example.test01.demo.controller;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.httpModel.CustomResponse;
import com.example.test01.demo.httpModel.auth.JwtAuthResponse;
import com.example.test01.demo.httpModel.auth.LogInRequest;
import com.example.test01.demo.httpModel.auth.LogInResponse;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.httpModel.auth.VerifyRequest;
import com.example.test01.demo.security.JwtProvider;
import com.example.test01.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthControllerUnitTest {

    @MockBean
    private UserService service;

    @Autowired
    private AuthController controller;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtProvider tokenProvider;

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
    void sing_up_success_for_a_valid_user_info(){
        final SignUpRequest rightRequest = new SignUpRequest();
        rightRequest.setEmail("test@test");
        rightRequest.setPhone("1975482967");
        rightRequest.setCountryCode("15");
        rightRequest.setPassword("123456");
        when(service.createUser(rightRequest)).thenReturn(Optional.of(validUser));
        final ResponseEntity<CustomResponse<LogInResponse>> controllerResponse = controller.signUp(rightRequest);
        assertEquals(0, Objects.requireNonNull(controllerResponse.getBody()).getData().getAuthyId());
    }

    @Test
    void sing_up_failure_for_invalid_user_info(){
        final SignUpRequest wrongRequest = new SignUpRequest();
        wrongRequest.setEmail("wrong@test");
        wrongRequest.setPhone("1975482967");
        wrongRequest.setCountryCode("15");
        wrongRequest.setPassword("123456");
        when(service.createUser(wrongRequest)).thenReturn(Optional.empty());
        final ResponseEntity<CustomResponse<LogInResponse>> wrongResponse = controller.signUp(wrongRequest);
        assertEquals(wrongResponse.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    void log_in_success_for_a_valid_user_info(){
        final LogInRequest rightRequest = new LogInRequest();
        rightRequest.setEmail("test@test");
        rightRequest.setPassword("123456");
        when(service.logIn(rightRequest)).thenReturn(Optional.of(validUser));
        final ResponseEntity<CustomResponse<LogInResponse>> controllerResponse = controller.logIn(rightRequest);
        assertEquals(0, Objects.requireNonNull(controllerResponse.getBody()).getData().getAuthyId());
    }

    @Test
    void log_in_failure_for_an_invalid_user_info(){
        final LogInRequest wrongRequest = new LogInRequest();
        wrongRequest.setEmail("wrog@test");
        wrongRequest.setPassword("123456");
        when(service.logIn(wrongRequest)).thenReturn(Optional.empty());
        final ResponseEntity<CustomResponse<LogInResponse>> wrongResponse = controller.logIn(wrongRequest);
        assertEquals(wrongResponse.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    void verify_success_by_valid_code(){
        final VerifyRequest rightRequest = new VerifyRequest();
        rightRequest.setAuthyId(123456);
        rightRequest.setCode("198765");
        when(service.verify(rightRequest)).thenReturn(Optional.of(validUser));
        final ResponseEntity<CustomResponse<JwtAuthResponse>> controllerResponse = controller.verify(rightRequest);
        assertEquals(controllerResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void verify_failure_by_invalid_code(){
        final VerifyRequest wrongRequest = new VerifyRequest();
        wrongRequest.setAuthyId(123456);
        wrongRequest.setCode("651824");
        when(service.verify(wrongRequest)).thenReturn(Optional.empty());
        final ResponseEntity<CustomResponse<JwtAuthResponse>> wrongResponse = controller.verify(wrongRequest);
        assertEquals(wrongResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}