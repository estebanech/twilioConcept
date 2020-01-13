package com.example.test01.demo.controller;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.httpModel.CustomErrorResponse;
import com.example.test01.demo.httpModel.CustomResponse;
import com.example.test01.demo.httpModel.CustomSuccessResponse;
import com.example.test01.demo.httpModel.auth.AccessResponse;
import com.example.test01.demo.httpModel.auth.JwtAuthResponse;
import com.example.test01.demo.httpModel.auth.LogInRequest;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.httpModel.auth.VerifyRequest;
import com.example.test01.demo.security.JwtProvider;
import com.example.test01.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<CustomResponse<AccessResponse>> signUp(final @Validated @RequestBody SignUpRequest request){
        final Optional<UserIn> user = userService.createUser(request);
        return user.<ResponseEntity<CustomResponse<AccessResponse>>>map(userIn ->
                ResponseEntity.ok(CustomSuccessResponse.success(
                        AccessResponse.builder()
                                .authyToken(jwtProvider.generateAuthy(userIn))
                                .build()))
        ).orElseGet(() -> ResponseEntity.badRequest().body(CustomErrorResponse.fail("Unable to create User")));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<AccessResponse>> logIn(final @Validated @RequestBody LogInRequest request){
        final Optional<UserIn> user = userService.logIn(request);
        return user.<ResponseEntity<CustomResponse<AccessResponse>>>map(userIn ->
                ResponseEntity.ok(CustomSuccessResponse.success(
                        AccessResponse.builder()
                                .authyToken(jwtProvider.generateAuthy(userIn))
                                .build()))
        ).orElseGet(() -> ResponseEntity.badRequest().body(CustomErrorResponse.fail("Unable to create User")));
    }

    @PostMapping("/verify")
    public ResponseEntity<CustomResponse<JwtAuthResponse>> verify(final @Validated @RequestBody VerifyRequest request){
        final Optional<UserIn> user = userService.verify(request);
        return user.<ResponseEntity<CustomResponse<JwtAuthResponse>>>map(
                userIn -> ResponseEntity.ok(CustomSuccessResponse.success(generateJWTResponse(userIn)))
        ).orElseGet(() -> ResponseEntity.badRequest().body(CustomErrorResponse.fail("Invalid Code")));
    }

    @GetMapping("/refresh")
    public ResponseEntity<CustomResponse<JwtAuthResponse>> refresh(final @Validated @RequestHeader(value = "refreshToken") String token){
        final Optional<UserIn> user = userService.refreshToken(token);
        return user.<ResponseEntity<CustomResponse<JwtAuthResponse>>>map(
                userIn -> ResponseEntity.ok(CustomSuccessResponse.success(generateJWTResponse(userIn)))
        ).orElseGet(() -> ResponseEntity.badRequest()
                .body(CustomErrorResponse.fail("Unable to Find a user for the Token")));
    }

    private JwtAuthResponse generateJWTResponse(final UserIn user){
        return JwtAuthResponse.builder()
                .authToken(jwtProvider.generateAuthToken(user))
                .refreshToken(jwtProvider.generateRefresh(user))
                .build();
    }
}
