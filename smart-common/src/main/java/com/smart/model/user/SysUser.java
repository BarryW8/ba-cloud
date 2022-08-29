package com.smart.model.user;

import com.smart.base.BaseModel;
import lombok.Data;

@Data
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
     * 用户状态0启用1停用
     */
    private int userStatus;

    /**
     * 登录次数
     */
    private int loginNum;

    /**
     * 最新登录时间
     */
    private String loginTime;

}
