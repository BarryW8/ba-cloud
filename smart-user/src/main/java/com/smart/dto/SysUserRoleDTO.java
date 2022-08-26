package com.smart.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SysUserRoleDTO {

    /**
     * 系统id
     */
    private Long id;
    /**
     * 用户id
     */
    @NotNull(message = "用户不能为空")
    private Long userId;
    /**
     * 角色id
     */
    @NotNull(message = "角色不能为空")
    private Long roleId;
}
