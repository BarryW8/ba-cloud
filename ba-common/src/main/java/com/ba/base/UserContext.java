package com.ba.base;

import com.ba.interceptor.CommonInterceptor;
import com.ba.token.TokenEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户上下文-当前线程
 */
@Setter
@Getter
public class UserContext {

    /**
     * 当前用户ID
     */
    private Long userId = -1000L;

    /**
     * 当前用户
     */
    private UserInfo userInfo;

    /**
     * 应用类型
     */
    private String deviceId;

    /**
     * 应用类型
     */
    private TokenEnum appType;

    /**
     * 平台类型
     */
    private String platform;

    public static UserContext get() {
        UserContext userContext = CommonInterceptor.threadLocal.get();
        if (userContext != null) {
            return userContext;
        }
        return new UserContext();
    }

    public static Long getUserId() {
        return get().userId;
    }

    public static UserInfo getUserInfo() {
        return get().userInfo;
    }

    public static String getDeviceId() {
        return get().deviceId;
    }

    public static TokenEnum getAppType() {
        return get().appType;
    }

    public static String getPlatform() {
        return get().platform;
    }

}
