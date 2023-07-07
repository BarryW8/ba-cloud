package com.ba.token;

import lombok.Data;

@Data
public class TokenModel {

    /**
     * 用户id
     */
    private String userId;

    /**
     * roleId
     */
    private String roleId;

    /**
     * app 类型
     */
    private String appType;

    /**
     * 平台类型 os android
     */
    private String platform;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 时间戳 — 创建时间
     */
    private String timestamp;

    /**
     * 过期时间
     */
    private String expire;
    /**
     * 密码10位
     */
    private String password10L;
}
