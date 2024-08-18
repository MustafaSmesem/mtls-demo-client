package com.joumer.mtlsclient.client;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CommonService {
    private final RestTemplate restTemplate;


    public CommonService(RestTemplate clientSslRestTemplate) {
        this.restTemplate = clientSslRestTemplate;
    }

    public ResponseEntity<String> sendHello(String deviceId) {
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        var entity = new HttpEntity<>(headers);

        var uri = UriComponentsBuilder.fromUriString("https://api-server.joumer.com:9091/hello")
                .queryParam("deviceId", deviceId)
                .build()
                .toUri();

        return restTemplate.exchange( uri, HttpMethod.GET, entity, String.class);
    }
}
