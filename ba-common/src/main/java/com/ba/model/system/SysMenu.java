package com.ba.model.system;

import com.ba.base.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseModel {

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型M目录C菜单
     */
    private String menuType;

    /**
     * 菜单标识 全局唯一
     */
    private String menuCode;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否外链0否1是
     */
    private int isFrame;

    /**
     * 路由路径
     */
    private String routePath;

    /**
     * 文件路径
     */
    private String pagePath;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 是否缓存0是1否
     */
    private int isCache;

    /**
     * 显示状态0显示1隐藏
     */
    private int visible;

    /**
     * 菜单状态0正常1停用
     */
    private int status;

}
