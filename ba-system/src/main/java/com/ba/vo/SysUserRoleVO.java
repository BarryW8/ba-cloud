package com.ba.vo;

import com.ba.model.system.SysUserRole;
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
