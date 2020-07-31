package com.taf.automation.ui.support.util;

import com.taf.automation.ui.support.TestProperties;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

public class CryptoUtils {
    private static final String CRYPTO_CIPHER = "AES";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 10000;
    private static final int KEY_LENGTH = 128;
    private final SecretKey secret = getKey();

    private SecretKey getKey() {
        try {
            String password = "SALT_KEY";
            String salt = getSalt(password);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), CRYPTO_CIPHER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getSalt(String key) {
        String automationKey = TestProperties.getInstance().getAutomationKey();
        if (StringUtils.isNotBlank(automationKey)) {
            try {
                return new String(new Hex().decode(automationKey.getBytes()));
            } catch (Exception ignore) {
                //
            }
        }

        File aut = new File("/etc/box/automation");
        if (aut.exists()) {
            try {
                List<String> lines = FileUtils.readLines(aut, Charset.defaultCharset());
                for (String line : lines) {
                    if (line.startsWith(key)) {
                        return line.replace(key + ":", "");
                    }
                }
            } catch (IOException ignore) {
                //
            }
        }

        return "salt";
    }

    @SuppressWarnings("unused")
    public String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(CRYPTO_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return new String(Base64.getEncoder().encode(cipher.doFinal(text.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(String text) {
        try {
            byte[] txt = Base64.getDecoder().decode(text);
            Cipher cipher = Cipher.getInstance(CRYPTO_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(txt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
