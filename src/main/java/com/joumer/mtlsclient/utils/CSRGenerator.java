package com.joumer.mtlsclient.utils;

import org.apache.hc.client5.http.utils.Base64;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class CSRGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static PKCS10CertificationRequest generateCSR(String deviceId, KeyPair keys) throws Exception {
        var subjectDN = String.format("CN=edfapay.com, O=EDFAPAYC, OU=EDFAPAYUNITC, L=SA, C=SA, ST=SA, E=admin@edfapay.com, UID=%s", deviceId);
        var x500Name = new X500Name(subjectDN);

        List<GeneralName> sanList = new ArrayList<>();
        sanList.add(new GeneralName(GeneralName.dNSName, "mpocapi.edfapay.com"));
        sanList.add(new GeneralName(GeneralName.dNSName, "mpocmobile.edfapay.com.com"));
        sanList.add(new GeneralName(GeneralName.dNSName, "mpocssl.edfapay.com"));
        sanList.add(new GeneralName(GeneralName.iPAddress, "8.213.44.105"));
        var generalNames = new GeneralNames(sanList.toArray(new GeneralName[0]));
        var sanExtensionValue = new ASN1EncodableVector();
        sanExtensionValue.add(generalNames);

        var p10Builder = new JcaPKCS10CertificationRequestBuilder(x500Name, keys.getPublic());
        p10Builder.addAttribute(Extension.subjectAlternativeName, generalNames);

        var csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        var signer = csBuilder.build(keys.getPrivate());

        return p10Builder.build(signer);
    }

    public static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(4096);
        return keyGen.generateKeyPair();
    }

    public static String parseCSR(PKCS10CertificationRequest csr) throws Exception {
        var sw = new StringWriter();
        var pemWriter = new JcaPEMWriter(sw);
        pemWriter.writeObject(csr);
        pemWriter.close();

        return sw.toString();
    }

    public static String encodePemPrivateKey(PrivateKey privateKey) throws Exception {
        var keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        var keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey decodedPrivateKey = keyFactory.generatePrivate(keySpec);

        String pemHeader = "-----BEGIN PRIVATE KEY-----\n";
        String pemFooter = "\n-----END PRIVATE KEY-----";
        String pemEncoded = Base64.encodeBase64String(decodedPrivateKey.getEncoded());

        return pemHeader + pemEncoded + pemFooter;
    }

    public static void writePrivateKeyToPem(PrivateKey privateKey, String filePath) throws IOException {
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(filePath))) {
            pemWriter.writeObject(privateKey);
        }
    }
}