package com.smart.vo;

import com.smart.model.user.SysUserRole;
import lombok.Data;

@Data
public class SysUserRoleVO extends SysUserRole {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 角色名
     */
    private String roleName;

}
