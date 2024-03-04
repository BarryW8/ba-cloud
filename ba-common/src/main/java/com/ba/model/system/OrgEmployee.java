package com.ba.model.system;

import com.ba.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: barryw
 * @Description: 员工管理
 * @Date: 2024/2/28 14:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgEmployee extends BaseModel {

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String code;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 组织名称
     */
    private String orgName;

}