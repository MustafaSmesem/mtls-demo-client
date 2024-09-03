package com.joumer.mtlsclient.config;


import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate defaultSslRestTemplate(RestTemplateBuilder builder, SslBundles sslBundles) {
        var sslBundle = sslBundles.getBundle("default");
        return builder.rootUri("https://8.213.44.105:8083").setSslBundle(sslBundle).build();
    }

    @Bean
    public RestTemplate clientSslRestTemplate(RestTemplateBuilder builder, SslBundles sslBundles) {
        var sslBundle = sslBundles.getBundle("rest");
        return builder.rootUri("https://api-server.joumer.com:9091").setSslBundle(sslBundle).build();
    }
}