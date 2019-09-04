package com.demo.main.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HMAC
{
      private static final Logger LOGGER = LoggerFactory.getLogger(HMAC.class);
      private static final String DIGEST="HmacSHA256";
    /*
     * Useful reference:
     * https://docs.oracle.com/javase/10/docs/api/javax/crypto/Mac.html
     * https://docs.oracle.com/javase/10/docs/specs/security/standard-names.html#mac-algorithms
     */

    public static String computeHash(String secret, String payload)
            throws InvalidKeyException, NoSuchAlgorithmException
    {
        Mac mac = Mac.getInstance(DIGEST);
        mac.init(new SecretKeySpec(secret.getBytes(), DIGEST));
		String base64Hash = new String(Base64.getEncoder().encode(mac.doFinal(payload.getBytes())));
        LOGGER.info("**Computed base4Hash is:" + base64Hash);
        return base64Hash;
    }

    public static boolean isHashValid(String secret, String payload, String verify)
            throws InvalidKeyException, NoSuchAlgorithmException
    {

        return verify.equals(computeHash(secret, payload));
    }
}
