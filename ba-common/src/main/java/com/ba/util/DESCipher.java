package com.ba.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

public class DESCipher {
    /**
     * DES加密，输入内容将被UTF-8编码后进行加密，密钥长度不要大于8位
     *
     * @param key 密钥
     * @param content 明文
     * @return 密文
     */
    public static String encryptByDES(String key, String content) {
        if ((key == null) || (content == null))
            return null;

        // 生成密钥，密钥长度限定为8位，如果超出8位取前8位
        byte[] tmpBytes;
        try {
            tmpBytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        byte[] keyBytes = new byte[8];
        for (int i = 0; i < tmpBytes.length && i < keyBytes.length; i++) {
            keyBytes[i] = tmpBytes[i];
        }
        // DES加密成为密文
        try {
            Key k = new SecretKeySpec(keyBytes, "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            byte[] output = cipher.doFinal(content.getBytes("UTF-8"));
            return ConvertUtil.bytesToHexString(output);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * DES解密，输入内容是密文，密钥长度不要大于8位
     *
     * @param key 密钥
     * @param cipherText 密文
     * @return 明文
     */
    public static String decryptByDES(String key, String cipherText) {
        if ((key == null) || (cipherText == null))
            return null;

        // 生成密钥，密钥长度限定为8位，如果超出8位取前8位
        byte[] tmpBytes;
        try {
            tmpBytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        byte[] keyBytes = new byte[8];
        for (int i = 0; i < tmpBytes.length && i < keyBytes.length; i++) {
            keyBytes[i] = tmpBytes[i];
        }
        // DES解密成为明文
        try {
            Key k = new SecretKeySpec(keyBytes, "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            byte[] output = cipher.doFinal(ConvertUtil.hexStringToBytes(cipherText));
            return new String(output, "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }

    public static void main(String[] args) {
        String key = "a1922c33e0151105";
        String m = DESCipher.encryptByDES(key,"testUser#21218cca77804d2ba1922c33e0151105");
       System.out.println(m);
        System.out.println(DESCipher.decryptByDES(key,m));
    }

}
