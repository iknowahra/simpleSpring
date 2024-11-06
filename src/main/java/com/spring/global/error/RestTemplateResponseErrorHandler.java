package com.spring.global.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    protected void handleError(ClientHttpResponse response, HttpStatusCode statusCode) throws IOException {
        // 응답 본문을 읽어옴
        String responseBody = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        log.warn("🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥{}", responseBody);


        String statusText = response.getStatusText();
        HttpHeaders headers = response.getHeaders();
        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);
        String message = "error ";//getErrorMessage(statusCode.value(), statusText, body, charset);

        RestClientResponseException ex;
        if (statusCode.is4xxClientError()) {
            ex = HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
        }
        else if (statusCode.is5xxServerError()) {
            ex = HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
        }
        else {
            ex = new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
        }


        throw ex;
    }
}