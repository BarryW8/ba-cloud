package com.ba.model.system;

import com.ba.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseModel {

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别0男1女
     */
    private int sex;

    /**
     * 用户状态 0正常 1锁定 2停用
     */
    private int userStatus;

    /**
     * 用户来源 0-WEB 1-APP
     */
    private int userSource;

    /**
     * 应用类型 多个逗号拼接
     */
    private String appType;

    /**
     * 登录次数
     */
    private int loginNum;

    /**
     * 最后登录时间
     */
    private String loginTime;

}
