package edu.ualberta.med.biobank.auth;

import javax.management.RuntimeErrorException;

import org.springframework.security.crypto.password.PasswordEncoder;

import edu.ualberta.med.biobank.util.StringEncrypter;
import edu.ualberta.med.biobank.util.StringEncrypter.EncryptionException;

public class BiobankPasswordEncoder implements PasswordEncoder {

    public BiobankPasswordEncoder() {
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            final var stringEncrypter = new StringEncrypter();
            return stringEncrypter.encrypt(rawPassword.toString());
        } catch (EncryptionException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            final var stringEncrypter = new StringEncrypter();
            return stringEncrypter.encrypt(rawPassword.toString()).equals(encodedPassword);
        } catch (EncryptionException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
