package com.example.practice.SBPractice.dumdum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReadCert {

    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
    private static final String TOKEN_CLAIM_ISSUER = "ccd50df1-3ba1-4d8f-832e-9f8c0ad2ed9c";
    private static final String TOKEN_CLAIM_SUBJECT = "ccd50df1-3ba1-4d8f-832e-9f8c0ad2ed9c";
    private static final String TOKEN_HEADER_TYP = "typ";
    private static final String TOKEN_HEADER_TYP_VALUE = "JWT";
    private static final String TOKEN_HEADER_ALG = "alg";
    private static final String TOKEN_HEADER_ALG_VALUE = "RS256";
    private static final String TOKEN_HEADER_X5T = "x5t";
    private static final String TOKEN_HEADER_X5T_VALUE = "+dCXYqvKAMV566R3IXOQF/VFdXY=";
    private static String ID = "d275ec63-de58-40ee-8e66-d2089a57fec6";

    private static PrivateKey getPrivateKey(){
        PrivateKey privateKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");

            URL res = ReadCert.class.getClassLoader().getResource("privateKey.key");

            String prKey = new String(Files.readAllBytes(Paths.get(res.toURI())));
            prKey = prKey.replaceAll("-----BEGIN PRIVATE KEY-----","")
                    .replaceAll("-----END PRIVATE KEY-----","")
                    .replaceAll("\\n", "").replaceAll("\\s", "");
            System.out.println(prKey);

            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prKey));
            privateKey = kf.generatePrivate(keySpecPKCS8);
        }catch(Exception e){
            e.printStackTrace();
        }
        return privateKey;
    }

    private static PublicKey getPublicKey(Certificate cert) {
        PublicKey pubKey = null;
        try {
            pubKey = cert.getPublicKey();
        }catch(Exception e){
            e.printStackTrace();
        }
        return pubKey;
    }

    private static String getThumbprint(Certificate cert)
            throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        String digestHex = DatatypeConverter.printHexBinary(digest);
        return digestHex.toLowerCase();
    }

    public static String generateToken(PrivateKey privateKey) {
        String jwtToken = "";
        try {
            Claims claims = Jwts.claims();
            claims.setIssuer(TOKEN_CLAIM_ISSUER).setIssuedAt(new Date(System.currentTimeMillis())).
                    setId(ID);
            claims.setAudience("https://login.microsoftonline.com/2c6104e8-71bb-4ad5-9be2-4d6321af2f5a/oauth2/token");
            claims.setExpiration(new Date(System.currentTimeMillis()+TimeUnit.DAYS.toMillis(365)));
            claims.setNotBefore(new Date(System.currentTimeMillis()));
            claims.setSubject(TOKEN_CLAIM_SUBJECT);

            JwtBuilder jwtBuilder = null;
            jwtBuilder = Jwts.builder().setClaims(claims).setHeaderParam(TOKEN_HEADER_TYP, TOKEN_HEADER_TYP_VALUE);
            jwtBuilder.setHeaderParam(TOKEN_HEADER_ALG,TOKEN_HEADER_ALG_VALUE);
            jwtBuilder.setHeaderParam(TOKEN_HEADER_X5T,TOKEN_HEADER_X5T_VALUE);
            jwtToken = jwtBuilder.signWith(signatureAlgorithm, privateKey).compact();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jwtToken;
    }

    public static String validateToken(String jwt, PublicKey publicKey) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwt).getBody();
            if((new Date(System.currentTimeMillis()).getTime()-claims.getIssuedAt().getTime()<= TimeUnit.DAYS.toMillis(365))
                    && claims.getIssuer().equals(TOKEN_CLAIM_ISSUER)) {
                System.out.println(claims.getId());
                return claims.getId();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        try {
            URL res = ReadCert.class.getClassLoader().getResource("certificate.crt");
            String input = res.getPath();

            FileInputStream fis = new FileInputStream(input);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(fis);
            fis.close();

            String thumbprint = getThumbprint(cert);
            System.out.println("/**Thumbprint**/");
            System.out.println(thumbprint);

            PublicKey publicKey = ReadCert.getPublicKey(cert);
            System.out.println("/**Public Key**/");
            System.out.println(publicKey);

            PrivateKey privateKey = getPrivateKey();
            System.out.println("/**Private Key**/");
            System.out.println(privateKey);

            String jwtToken = ReadCert.generateToken(privateKey);
            System.out.println("/**JWT token**/");
            System.out.println(jwtToken);

            String jti = validateToken(jwtToken,publicKey);
            System.out.println("/**JWT ID**/");
            System.out.println(jti);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
