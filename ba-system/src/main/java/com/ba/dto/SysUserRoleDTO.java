package com.ba.dto;

import com.ba.model.system.SysUserRole;
import lombok.Data;

import java.util.List;

@Data
public class SysUserRoleDTO {

    /**
     * 系统id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 保存的数据
     */
    private List<SysUserRole> userRoles;

}
