package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.Rand;
import org.apache.commons.codec.binary.Hex;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Creating and testing a new crypto key
 */
public class CreateKeyTest {
    private String createRandomKey() {
        String key = Rand.letters(5) + "-" + Rand.letters(5) + "-" + Rand.letters(5) + "-" + Rand.letters(5);
        return key.toUpperCase();
    }

    private String getEncodeKey(String key) {
        return new String(new Hex().encode(key.getBytes()));
    }

    private String getDecodedKey(String key) {
        try {
            return new String(new Hex().decode(key.getBytes()));
        } catch (Exception ex) {
            return null;
        }
    }

    private void validateKey(String value) {
        String encrypt = new CryptoUtils().encrypt(value);
        String decrypt = new CryptoUtils().decrypt(encrypt);
        assertThat("Decryption", decrypt, equalTo(value));

        String log = "*****" + "\n" + "Test Value:  " + value + "\n" + "Encrypted Value:  " + encrypt + "\n" + "*****" + "\n";
        Helper.log(log, true);
    }

    @Test
    public void performRandomAutomationKeyGeneration() {
        String randomKey = createRandomKey();
        String encodedKey = getEncodeKey(randomKey);
        String decodedKey = getDecodedKey(encodedKey);
        assertThat("Decoded Key", decodedKey, equalTo(randomKey));
        Helper.log("automation.key=" + encodedKey, true);
        Helper.log("Decoded Key=" + decodedKey, true);
    }

    /**
     * Add the key generated from performRandomAutomationKeyGeneration to the test.properties file to validate the
     * key in this test
     */
    @Test
    public void validateKeyTestWithStaticValue() {
        // With automation.key=554f4958552d414e4c48592d49454344452d484c494554
        String value = "Pm3yyK5EyP98QJrpTcrhgvn3K";
        String encrypted = "YRVx9vum59nUh+sUJQClOZIPe9Yt2fIFJPIB7edSsoM=";
        String decrypt = new CryptoUtils().decrypt(encrypted);
        assertThat("Decryption with key:  554f4958552d414e4c48592d49454344452d484c494554", decrypt, equalTo(value));
        Helper.log("Decrypt Value = " + decrypt, true);
    }

    @Test
    public void validateKeyTestWithRandomAlphanumericValue() {
        validateKey(Rand.alphanumeric(16, 32));
    }

    @Test
    public void validateKeyTestWithRandomNumbersOnlyValue() {
        validateKey(Rand.numbers(5, 10));
    }

    @Test
    public void validateKeyTestWithRandomLettersOnlyValue() {
        validateKey(Rand.letters(5, 10));
    }

    @Test
    public void validateKeyTestWithRandomSpecialOnlyValue() {
        validateKey(Rand.onlyChars(Rand.randomRange(5, 10), Rand.getSpecialDefaults()));
    }

    @Test
    public void validateKeyTestWithRandomPasswordValue() {
        String chars = Rand.getLettersDefaults() + Rand.getNumbersDefaults() + Rand.getSpecialDefaults();
        validateKey(Rand.onlyChars(Rand.randomRange(16, 32), chars));
    }

}
