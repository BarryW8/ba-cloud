package com.smart.vo;

import com.smart.vo.MenuVO;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {

    /**
     * 用户名
     */
    private String userName;

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

    List<SysMenuVO> menuList;
}
