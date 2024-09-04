package com.joumer.mtlsclient.controller;

import com.joumer.mtlsclient.client.CertificateService;
import com.joumer.mtlsclient.model.CertificateSignResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final CertificateService certificateService;

    public AuthController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }


    @GetMapping("get-certificate")
    public ResponseEntity<CertificateSignResponse> getCertificate(@RequestParam String deviceId) throws Exception {
        return ResponseEntity.ok(certificateService.getSignedCertificate(deviceId));
    }
}
