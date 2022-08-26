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
    private String trueName;

    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别 0男 1女
     */
    private int sex;

    /**
     * 用户状态:0启用 1停用
     */
    private int userStatus;

}
