/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.utils;

import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author bd_dev_murtala
 */
public class HashPassword {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        String hashPassword = Hashing.sha512().hashString(password, Charset.forName("UTF-8")).toString();
        return hashPassword;
    }

    public static boolean isEqual(String plainText, String hashed) throws NoSuchAlgorithmException {
        if (plainText != null && hashed != null) {
            if (hashPassword(plainText).equals(hashed)) {
                return true;
            }
        }
        return false;
    }

}
