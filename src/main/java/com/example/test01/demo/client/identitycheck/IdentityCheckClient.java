package com.example.test01.demo.client.identitycheck;

import com.example.test01.demo.client.identitycheck.model.Identity;
import com.example.test01.demo.client.identitycheck.model.IdentityResponse;

public interface IdentityCheckClient {
    IdentityResponse checkIdentity(final Identity identity);
}
