package edu.ualberta.med.biobank.util;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * The source code from this file comes from caCORE CSMAPI 4.1.
 *
 * It was decompiled using http://www.javadecompilers.com/ from "csmapi.jar".
 */
public class StringEncrypter {

    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private static KeySpec keySpec;
    private static SecretKeyFactory keyFactory;
    private static SecretKey key;
    private static final String UNICODE_FORMAT = "UTF-8";

    public class EncryptionException extends Exception {
        public EncryptionException(Throwable cause) {
            super(cause);
        }
    }

    private static String getKey() {
        return "123CSM34567890ENCRYPTIONC3PR4KEY5678901234567890";
    }

    public StringEncrypter() throws StringEncrypter.EncryptionException {
        this("DESede", getKey());
    }

    private StringEncrypter(final String encryptionScheme, final String encryptionKey)
        throws StringEncrypter.EncryptionException {
        if (encryptionKey == null) {
            throw new IllegalArgumentException("encryption key was null");
        }
        if (encryptionKey.trim().length() < 24) {
            throw new IllegalArgumentException("encryption key was less than 24 characters");
        }
    }

    public synchronized String encrypt(final String unencryptedString) throws StringEncrypter.EncryptionException {
        if (unencryptedString == null || unencryptedString.trim().length() == 0) {
            throw new IllegalArgumentException("unencrypted string was null or empty");
        }
        Cipher ecipher;
        try {
            final byte[] keyAsBytes = getKey().getBytes("UTF-8");
            StringEncrypter.keySpec = new DESedeKeySpec(keyAsBytes);
            StringEncrypter.keyFactory = SecretKeyFactory.getInstance("DESede");
            StringEncrypter.key = StringEncrypter.keyFactory.generateSecret(StringEncrypter.keySpec);
            ecipher = Cipher.getInstance("DESede");
            final Cipher dcipher = Cipher.getInstance("DESede");
            ecipher.init(1, StringEncrypter.key);
            final AlgorithmParameters ap = ecipher.getParameters();
            dcipher.init(2, StringEncrypter.key, ap);
        } catch (InvalidKeyException e) {
            throw new StringEncrypter.EncryptionException((Throwable) e);
        } catch (UnsupportedEncodingException e2) {
            throw new StringEncrypter.EncryptionException((Throwable) e2);
        } catch (NoSuchAlgorithmException e3) {
            throw new StringEncrypter.EncryptionException((Throwable) e3);
        } catch (NoSuchPaddingException e4) {
            throw new StringEncrypter.EncryptionException((Throwable) e4);
        } catch (InvalidAlgorithmParameterException e5) {
            throw new StringEncrypter.EncryptionException((Throwable) e5);
        } catch (InvalidKeySpecException e6) {
            throw new StringEncrypter.EncryptionException((Throwable) e6);
        }
        try {
            final byte[] cleartext = unencryptedString.getBytes("UTF-8");
            final byte[] ciphertext = ecipher.doFinal(cleartext);
            return Base64.getEncoder().encodeToString(ciphertext);
        } catch (Exception e7) {
            throw new StringEncrypter.EncryptionException((Throwable) e7);
        }
    }

    public synchronized String decrypt(final String encryptedString) throws StringEncrypter.EncryptionException {
        if (encryptedString == null || encryptedString.trim().length() <= 0) {
            throw new IllegalArgumentException("encrypted string was null or empty");
        }
        Cipher dcipher;
        try {
            final byte[] keyAsBytes = getKey().getBytes("UTF-8");
            StringEncrypter.keySpec = new DESedeKeySpec(keyAsBytes);
            StringEncrypter.keyFactory = SecretKeyFactory.getInstance("DESede");
            StringEncrypter.key = StringEncrypter.keyFactory.generateSecret(StringEncrypter.keySpec);
            final Cipher ecipher = Cipher.getInstance("DESede");
            dcipher = Cipher.getInstance("DESede");
            ecipher.init(1, StringEncrypter.key);
            final AlgorithmParameters ap = ecipher.getParameters();
            dcipher.init(2, StringEncrypter.key, ap);
        } catch (InvalidKeyException e) {
            throw new StringEncrypter.EncryptionException((Throwable) e);
        } catch (UnsupportedEncodingException e2) {
            throw new StringEncrypter.EncryptionException((Throwable) e2);
        } catch (NoSuchAlgorithmException e3) {
            throw new StringEncrypter.EncryptionException((Throwable) e3);
        } catch (NoSuchPaddingException e4) {
            throw new StringEncrypter.EncryptionException((Throwable) e4);
        } catch (InvalidAlgorithmParameterException e5) {
            throw new StringEncrypter.EncryptionException((Throwable) e5);
        } catch (InvalidKeySpecException e6) {
            throw new StringEncrypter.EncryptionException((Throwable) e6);
        }
        try {
            final byte[] cleartext = Base64.getDecoder().decode(encryptedString);
            final byte[] ciphertext = dcipher.doFinal(cleartext);
            return bytes2String(ciphertext);
        } catch (Exception e7) {
            throw new StringEncrypter.EncryptionException((Throwable) e7);
        }
    }

    private static String bytes2String(final byte[] bytes) {
        final StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }
}
