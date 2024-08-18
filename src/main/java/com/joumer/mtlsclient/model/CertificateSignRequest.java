package com.joumer.mtlsclient.model;

public record CertificateSignRequest(String pemCsr, String deviceId) {
}
