package com.joumer.mtlsclient.model;

public record CertificateSignResponse(
        String data,
        String code,
        String message
) {
}
