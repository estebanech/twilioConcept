package com.example.test01.demo.controller;

import com.example.test01.demo.client.identitycheck.IdentityCheckClient;
import com.example.test01.demo.client.identitycheck.model.Identity;
import com.example.test01.demo.client.identitycheck.model.IdentityResponse;
import com.example.test01.demo.httpModel.CustomResponse;
import com.example.test01.demo.httpModel.CustomSuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/verify")
@AllArgsConstructor
public class IdentityCheckController {
    private final IdentityCheckClient client;

    @GetMapping
    public ResponseEntity<CustomResponse<IdentityResponse>> verifyPerson(
            @RequestParam(name = "id") final Long id,
            @RequestParam(name = "name") final String name,
            @RequestParam(name = "age") final String age){
        return ResponseEntity.ok(
                CustomSuccessResponse.success(client.checkIdentity(
                        Identity.builder()
                                .id(id)
                                .name(name)
                                .age(age)
                                .build())));
    }
}
