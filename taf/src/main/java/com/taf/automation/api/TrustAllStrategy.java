package com.taf.automation.api;

import org.apache.http.conn.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * A trust strategy that accepts all certificates.<BR>
 * <B>Note: </B>This should not be used in production as this is susceptible to any man in the middle attack.<BR>
 */
public class TrustAllStrategy implements TrustStrategy {
    @Override
    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        return true;
    }

}
