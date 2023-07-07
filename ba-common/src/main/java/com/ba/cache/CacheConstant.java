package com.ba.cache;

/**
 * redis 缓存前缀
 */
public class CacheConstant {

    public static final String CACHE_PREFIX = "barry:netdisk:";

    /**
     * 系统菜单
     */
    public static final String CACHE_HASH_KEY_SYS_MENU = CACHE_PREFIX + "menu";
    /**
     * Token
     * key = 应用登录类型appType:临时用户id
     */
    public static final String CACHE_STR_KEY_TOKEN_KEY = CACHE_PREFIX + "token:%s:%s";
    /**
     * 角色权限
     * key = 角色id
     */
    public static final String CACHE_STR_KEY_ROLE_PERMISSION = CACHE_PREFIX + "role:permission:%s";
    /**
     * web 登录图形验证码
     * key = IP + (User-Agent)
     */
    public static final String CACHE_STR_KEY_WEB_USER_CAPTCHA = CACHE_PREFIX + "web:captcha:%s";
    /**
     * web 修改密码
     * key = 用户id
     */
    public static final String CACHE_STR_KEY_WEB_USER_CHANGE_PASSWORD = CACHE_PREFIX + "web:changePassword:%s";

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
