package com.example.test01.demo.client.intellexer;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

@Data
@Builder
public class IntellexerRequest<T,R> {
    private T request;

    private Class<R> clazz;

    private String path;

    @Builder.Default private HttpMethod method = HttpMethod.GET;

    private MultiValueMap<String,String> params;
}
