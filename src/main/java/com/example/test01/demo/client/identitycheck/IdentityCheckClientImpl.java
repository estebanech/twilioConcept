package com.example.test01.demo.client.identitycheck;

import com.example.test01.demo.client.identitycheck.model.Identity;
import com.example.test01.demo.client.identitycheck.model.IdentityRequest;
import com.example.test01.demo.client.identitycheck.model.IdentityResponse;
import com.example.test01.demo.client.lord.exception.LordClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@AllArgsConstructor
public class IdentityCheckClientImpl implements IdentityCheckClient{

    private final IdentityCheckProperties properties;

    @Qualifier("identityRestTemplate")
    private final RestTemplate restTemplate;

    private HttpHeaders getHeaders(){
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private <T, R> R makeRequest(final IdentityCheckRequest<T, R> identityCheckRequest){
        log.info("Request URL: {}", identityCheckRequest.getPath());
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(properties.getUrl())
                .path(identityCheckRequest.getPath())
                .queryParams(identityCheckRequest.getParams());
        final HttpHeaders headers = getHeaders();
        final HttpEntity<T> entity = new HttpEntity<>(
                identityCheckRequest.getRequest(), headers);

        log.info("Request: {}", entity.toString());

        final ResponseEntity<R> response;
        response = restTemplate.exchange(
                builder.build().encode().toUri(),
                identityCheckRequest.getMethod(),
                entity,
                identityCheckRequest.getClazz());
        log.info("Response: {}", response.toString());
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Request error {}", response);
            throw new LordClientException(response);
        }
        return response.getBody();
    }

    @Override
    public IdentityResponse checkIdentity(final Identity identity){
        final MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("name",identity.getName());
        params.add("age",identity.getAge());
        final IdentityCheckRequest<IdentityRequest,IdentityResponse> request=
                IdentityCheckRequest.<IdentityRequest,IdentityResponse>builder()
                        .path(String.format("/compare/%d",identity.getId()))
                        .clazz(IdentityResponse.class)
                        .params(params)
                        .build();
        return makeRequest(request);
    }

}
