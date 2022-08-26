package com.smart.model.user;


import com.smart.base.BaseModel;
import lombok.Data;

/**
 * 角色菜单中间表
 */
@Data
public class SysRoleMenu extends BaseModel {


    private static final long serialVersionUID = -9107193122489973428L;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 菜单id
     */
    private Long menuId;
    /**
     * 权限id字符串
     */
    private String menuAction;
}
