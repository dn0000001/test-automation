package com.taf.automation.ui.support.converters;

import org.apache.commons.lang3.BooleanUtils;

public class DynamicCredentials {
    private static final String SEPARATOR = ":";
    private String role;
    private String user;
    private String password;
    private boolean decrypt;

    /**
     * Constructor to take the formatted string and initialize the object<BR>
     * <B>Format: </B> ROLE:USER:PASSWORD:DECRYPT<BR>
     * <B>Notes:</B>
     * <UL>
     * <LI>String must contain 3 colons to separate the information or an exception will occur</LI>
     * <LI>ROLE is used to access the alias</LI>
     * <LI>USER is the ID to log into the account</LI>
     * <LI>PASSWORD is the encrypted/decrypted password for the account</LI>
     * <LI>DECRYPT is used to indicate if the password is encrypted/decrypted</LI>
     * </UL>
     *
     * @param dynamicCredentials - String that contains the Dynamic Credentials
     */
    public DynamicCredentials(String dynamicCredentials) {
        String[] cred = dynamicCredentials.split(SEPARATOR);
        role = cred[0].trim();
        user = cred[1].trim();
        password = cred[2].trim();
        decrypt = BooleanUtils.toBoolean(cred[3].trim());
    }

    public String getRole() {
        return role;
    }

    public boolean isDecrypt() {
        return decrypt;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return role + SEPARATOR + user + SEPARATOR + password + SEPARATOR + decrypt;
    }

}
