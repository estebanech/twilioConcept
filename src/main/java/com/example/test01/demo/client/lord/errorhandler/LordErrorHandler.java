package com.example.test01.demo.client.lord.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class LordErrorHandler extends DefaultResponseErrorHandler {
        @Override
        public void handleError(final ClientHttpResponse response) throws IOException {
        if(response != null)
        {
            log.error("Response error: {}", response.getStatusText());
            if (response.getBody() != null && response.getBody().markSupported())
            {
                log.error(IOUtils.toString(response.getBody(), Charset.defaultCharset()));
                response.getBody().reset();
            }
        }
        else{
            log.error("Null response error");
        }
    }
    }
