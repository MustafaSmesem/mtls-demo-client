package com.joumer.mtlsclient.client;


import com.joumer.mtlsclient.model.CertificateSignRequest;
import com.joumer.mtlsclient.model.CertificateSignResponse;
import com.joumer.mtlsclient.utils.CSRGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;


@Service
public class CertificateService {

    private final RestTemplate restTemplate;

    public CertificateService(RestTemplate defaultSslRestTemplate) {
        this.restTemplate = defaultSslRestTemplate;
    }

    private CertificateSignResponse signCertificate(String certificate, String deviceId) {
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        var csr = new CertificateSignRequest(certificate, deviceId);
        var request = new HttpEntity<>(csr, headers);
        return restTemplate.postForObject( "https://mpocssl.edfapay.com:8083/ssl/sign-certificate", request, CertificateSignResponse.class);
    }

    public CertificateSignResponse getSignedCertificate(String deviceId) throws Exception {
        var keyPairs = CSRGenerator.generateRSAKeyPair();
        var csr = CSRGenerator.generateCSR(deviceId, keyPairs);
        var csrStr = CSRGenerator.parseCSR(csr);
        CSRGenerator.writePrivateKeyToPem(keyPairs.getPrivate(), "/Users/mustafa/Desktop/client_certificate/client.key");
        var signedCertificate = signCertificate(csrStr, deviceId);
        writeData(signedCertificate.data(), "/Users/mustafa/Desktop/client_certificate/client.crt");
        return signedCertificate;
    }

    private void writeData(String data, String fileName) {
        try {
            var writer = new FileWriter(fileName);
            writer.write(data);
            writer.close();
            System.out.println("File overwritten successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while overwriting the file.");
            e.printStackTrace();
        }
    }

}