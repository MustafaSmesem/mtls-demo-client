package com.joumer.mtlsclient.client;


import com.joumer.mtlsclient.model.CertificateSignRequest;
import com.joumer.mtlsclient.utils.CSRGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;


@Service
public class CertificateService {

    private final RestTemplate restTemplate;

    public CertificateService(RestTemplate defaultSslRestTemplate) {
        this.restTemplate = defaultSslRestTemplate;
    }

    private String signCertificate(String certificate, String deviceId) {
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        var csr = new CertificateSignRequest(certificate, deviceId);
        var request = new HttpEntity<>(csr, headers);
        return restTemplate.postForObject( "https://api-server.joumer.com:9090/auth/certificate-sign", request, String.class);
    }

    public String getSignedCertificate(String deviceId) throws Exception {
        var keyPairs = CSRGenerator.generateRSAKeyPair();
        var csr = CSRGenerator.generateCSR(deviceId, keyPairs);
        var csrStr = CSRGenerator.parseCSR(csr);
        var signedCertificate = signCertificate(csrStr, deviceId);
        writeData(signedCertificate, "/Users/mustafa/test/mtls-client/work/certs/client.crt");
        CSRGenerator.writePrivateKeyToPem(keyPairs.getPrivate(), "/Users/mustafa/test/mtls-client/work/certs/client.key");

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