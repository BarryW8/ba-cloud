package com.smart.cache;

/**
 * redis 缓存前缀
 */
public class CacheConstant {

    /**
     * token redis key
     */
    public static final String TOKEN_KEY = "smart:token:";
    /**
     * 在线用户
     */
    public static final String CACHE_KEY_USER_LOGIN = "system:loginUser:";
    /**
     * 系统用户
     */
    public static final String CACHE_KEY_USER_INFO = "system:userInfo:";
    /**
     * 系统菜单
     */
    public static final String CACHE_KEY_SYS_MENU = "system:sysMenu";
    /**
     * 手机验证码信息 redis key
     */
    public static final String TELEPHONE_CODE = "parking:telephone:";
    /**
     * 微信用户注册
     */
    public static final String WX_REGISTER_CODE = "parking:register:%s:%s:%s";

}
