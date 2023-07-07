package com.ba.enums;

import lombok.Getter;

/**
 * 响应状态码
 */
public enum ResEnum {

    /**
     * 接口
     */
    SYSTEM_NO_PERMISSION(150010, "权限不足"),
    /**
     * 账号
     */
    ACCOUNT_REPEAT(250001, "账号已经存在"),
    ACCOUNT_UNREGISTER(250002, "账号不存在"),
    ACCOUNT_PWD_ERROR(250003, "账号或者密码错误"),
    ACCOUNT_EXPIRED(250004, "登录过期，请重新登录"),
    ACCOUNT_CUSTOMER_ERROR(250005, "客户信息不存在"),
    ACCOUNT_COMPANY_ERROR(250006, "未授权商家"),
    ACCOUNT_ROLE_ERROR(250007, "未授权角色"),
    ACCOUNT_PARKING_ERROR(250008, "未授权车场"),
    ACCOUNT_CAPTCHA_ERROR(250009, "验证码错误"),
    ACCOUNT_LOCKED(250010, "账号已锁定"),
    ACCOUNT_DISABLED(250011, "账号已停用"),
    ACCOUNT_EXCEPTION(250012, "账号异常"),
    ACCOUNT_OFFLINE(250013, "账号已下线"),
    /**
     * 微信用户注册
     */
    PHONE_EMPTY(250050, "手机号码不能为空"),
    SMS_EMPTY(250051, "短信验证码不能为空"),
    SMS_OVERDUE(250052, "短信验证码已过期"),
    SMS_ERROR(250053, "短信验证码不正确"),
    /**
     * 流控操作
     */
    CONTROL_FLOW(500101, "限流控制"),
    CONTROL_DEGRADE(500201, "降级控制"),
    CONTROL_AUTH(500301, "认证控制"),
    /**
     * 查询绑定
     */
    FIND_BIND_NO_CUSTOMER(250501, "客户未授权"),
    FIND_BIND_OPENID_EMPTY(250502, "openid为空"),
    FIND_BIND_USERID_EMPTY(250503, "支付宝userid为空"),
    FIND_BIND_ALI_APP_ID_EMPTY(250504, "阿里的appid为空"),
    FIND_BIND_APP_TYPE(250505, "app类型不正确"),
    FIND_BIND_APP_ID_EMPTY(250506, "appid为空"),
    FIND_BIND_BINDING(250507, "已绑定"),
    FIND_BIND_UNBINDING(250508, "未绑定"),
    ;

    @Getter
    private int code;

    @Getter
    private String msg;

    private ResEnum(int code, String message) {
        this.code = code;
        this.msg = message;
    }


}
