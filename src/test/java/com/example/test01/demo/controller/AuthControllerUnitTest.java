package com.example.test01.demo.controller;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.httpModel.auth.LogInRequest;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.httpModel.auth.VerifyRequest;
import com.example.test01.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ActiveProfiles("test")
@SpringBootTest
class AuthControllerUnitTest {

    @MockBean
    private UserService service;

    @Autowired
    private AuthController controller;

    @Autowired
    private PasswordEncoder encoder;

    private UserIn validUser;

    private MockMvc mockMvc;

    private ObjectMapper mapper;


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
        this.mockMvc = standaloneSetup(this.controller).build();
        this.mapper = new ObjectMapper();
    }

    private <T> void doPostRequestSuccessTest(final String path, final T request, final String expectedJsonResponse) throws Exception{
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonResponse));
    }
    private <T> void doPostRequestForJwtSuccessTest(final String path, final T request) throws Exception{
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    private <T> void doPostRequestFailureTest(final String path, final T request) throws Exception{
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sing_up_success_for_a_valid_user_info() throws Exception {
        final SignUpRequest rightRequest = new SignUpRequest();
        rightRequest.setEmail("test@test");
        rightRequest.setPhone("1975482967");
        rightRequest.setCountryCode("15");
        rightRequest.setPassword("123456");
        when(service.createUser(rightRequest)).thenReturn(Optional.of(validUser));
        doPostRequestSuccessTest("/api/auth/signup",rightRequest,
                "{'data':{'authyId':0},'success':true}");
    }

    @Test
    void sing_up_failure_for_invalid_user_info() throws Exception {
        final SignUpRequest wrongRequest = new SignUpRequest();
        wrongRequest.setEmail("wrong@test");
        wrongRequest.setPhone("1975482967");
        wrongRequest.setCountryCode("15");
        wrongRequest.setPassword("123456");
        when(service.createUser(wrongRequest)).thenReturn(Optional.empty());
        doPostRequestFailureTest("/api/auth/signup",wrongRequest);
    }

    @Test
    void log_in_success_for_a_valid_user_info() throws Exception{
        final LogInRequest rightRequest = new LogInRequest();
        rightRequest.setEmail("test@test");
        rightRequest.setPassword("123456");
        when(service.logIn(rightRequest)).thenReturn(Optional.of(validUser));
        doPostRequestSuccessTest("/api/auth/login",rightRequest,
                "{'data':{'authyId':0},'success':true}");
    }

    @Test
    void log_in_failure_for_an_invalid_user_info() throws Exception {
        final LogInRequest wrongRequest = new LogInRequest();
        wrongRequest.setEmail("wrog@test");
        wrongRequest.setPassword("123456");
        when(service.logIn(wrongRequest)).thenReturn(Optional.empty());
        doPostRequestFailureTest("/api/auth/login",wrongRequest);
    }

    @Test
    void verify_success_by_valid_code() throws Exception {
        final VerifyRequest rightRequest = new VerifyRequest();
        rightRequest.setAuthyId(123456);
        rightRequest.setCode("198765");
        when(service.verify(rightRequest)).thenReturn(Optional.of(validUser));
        doPostRequestForJwtSuccessTest("/api/auth/verify",rightRequest);
    }

    @Test
    void verify_failure_by_invalid_code() throws Exception {
        final VerifyRequest wrongRequest = new VerifyRequest();
        wrongRequest.setAuthyId(123456);
        wrongRequest.setCode("651824");
        when(service.verify(wrongRequest)).thenReturn(Optional.empty());
        doPostRequestFailureTest("/api/auth/verify",wrongRequest);
    }
}