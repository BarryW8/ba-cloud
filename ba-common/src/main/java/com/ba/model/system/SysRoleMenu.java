package com.ba.model.system;


import com.ba.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色菜单中间表
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
     * 菜单编号 全局唯一
     */
    private String menuCode;

    /**
     * 按钮权限 字典
     */
    private String permission;

}
