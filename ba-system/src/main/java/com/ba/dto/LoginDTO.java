package com.ba.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class LoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 验证码
     */
//    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 登录类型0账户密码1扫码登录
     */
    private int loginType;

}
