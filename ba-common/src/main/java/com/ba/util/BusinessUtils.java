package com.ba.util;

/**
 * @Author: barryw
 * @Description: 业务相关公共方法类
 * @Date: 2024/2/23 14:54
 */
public class BusinessUtils {

    /**
     * 随机生成临时用户id
     */
    public static String getTempUserId(Long userId, boolean isAll) {
        String prefix = "ba";
        if (isAll) {
            return prefix + "*" + userId.toString() + "*";
        } else {
            String firstRandomNum = CommonUtils.getRandomCode(1);
            String secondRandomNum = CommonUtils.getRandomCode(3);
            return prefix + firstRandomNum + userId.toString() + secondRandomNum;
        }
    }

}
