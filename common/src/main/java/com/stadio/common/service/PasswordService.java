package com.stadio.common.service;

import com.stadio.common.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Andy on 7/14/2017.
 */
public class PasswordService
{
    private static Logger logger = LogManager.getLogger(PasswordService.class);
    private static final String ENCODING = "UTF-8";
    private static final String DIVIDER = "|||";

    public static boolean validPassword(String pwd, String noise, String hash)
    {
        String h = StringUtils.identifier256(pwd + DIVIDER + noise);
        return h.equals(hash);
    }

    public static String hidePassword(String pwd, String noise)
    {
        return StringUtils.identifier256(pwd + DIVIDER + noise);
    }

    public static String encrypt(String key, String initVector, String value)
    {
        try
        {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(ENCODING));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(ENCODING), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            logger.info("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted)
    {
        try
        {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(ENCODING));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(ENCODING), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        }
        catch (Exception ex)
        {
            logger.debug("decrypt exception: ", ex);
        }

        return null;
    }
}
