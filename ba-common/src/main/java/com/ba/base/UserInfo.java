package com.ba.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 508445227960868741L;

    /**
     * 用户token
     */
    private String token;

    /**
     * 用户id
     */
    private Long id;

    /**
     * token类型
     */
    private String type;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 是否是超级管理员
     */
    private boolean superManager;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private int sex;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 三方用户id
     */
    private Long mobUserAppId;

}
