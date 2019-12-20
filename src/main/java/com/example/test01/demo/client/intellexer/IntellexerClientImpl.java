package com.example.test01.demo.client.intellexer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

@Service
public class IntellexerClientImpl implements IntellexerClient {
    @Value("${app.intellexerKey}")
    private String apiKey;

    private MultiValueMap<String,String> getAnalyzeParams(){
        final MultiValueMap<String,String> params= new LinkedMultiValueMap<>();
        params.put("apikey", Collections.singletonList(apiKey));
        params.put("loadSentences", Collections.singletonList("True"));
        params.put("loadTokens", Collections.singletonList("True"));
        params.put("loadRelations", Collections.singletonList("True"));
        return null;
    }
}
