package com.ba.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class DesUtils {
    /**
     * 加密
     *
     * @param info
     * @return
     */
    public static String encrypt(String info, String secretKey) {
        byte[] key = new byte[0];
        try {
            key = new BASE64Decoder().decodeBuffer(secretKey);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        DES des = SecureUtil.des(key);
        String encrypt = des.encryptHex(info);
        return encrypt;
    }

    /**
     * 解密
     *
     * @param encrypt
     * @return
     */
    public static String decode(String encrypt, String secretKey) throws IOException {
        byte[] key = new BASE64Decoder().decodeBuffer(secretKey);
        DES des = SecureUtil.des(key);
        return des.decryptStr(encrypt);
    }

    public static void main(String[] args) throws IOException {
        //生成密钥，并转为字符串，可以储存起来，解密时可直接使用
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
        String secretKey = new BASE64Encoder().encodeBuffer(key);
        System.out.println(secretKey);
        String admin = DesUtils.encrypt("20221021009", secretKey);
        System.out.println("加密后="+admin);

        // 解密
        String adminDecode = DesUtils.decode(admin, secretKey);
        System.out.println("解密后="+adminDecode);
    }

}
