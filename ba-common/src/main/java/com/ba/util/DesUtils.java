package com.ba.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

import java.io.IOException;
import java.util.Base64;

public class DesUtils {
    /**
     * 加密
     *
     * @param info
     * @return
     */
    public static String encrypt(String info, String secretKey) {
        byte[] key = new byte[0];
        key = Base64.getDecoder().decode(secretKey);
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
        byte[] key = Base64.getDecoder().decode(secretKey);
        DES des = SecureUtil.des(key);
        return des.decryptStr(encrypt);
    }

    public static void main(String[] args) throws IOException {
        //生成密钥，并转为字符串，可以储存起来，解密时可直接使用
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println(secretKey);
        String admin = DesUtils.encrypt("20221021009", secretKey);
        System.out.println("加密后="+admin);

        // 解密
        String adminDecode = DesUtils.decode(admin, secretKey);
        System.out.println("解密后="+adminDecode);
    }

}
