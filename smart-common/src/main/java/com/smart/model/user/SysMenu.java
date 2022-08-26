package com.smart.model.user;

import com.smart.base.BaseModel;
import lombok.Data;

@Data
public class SysMenu extends BaseModel {

    /**
     * 菜单标题
     */
    private String label;


    /**
     * 上级id
     */
    private Long parentId;

    /**
     * vue文件路径
     */
    private String pagePath;

    /**
     * 路由路径
     */
    private String routePath;

    /**
     * 图标路径
     */
    private String iconPath;

    /**
     * 跳转类型:0-内，1-外
     */
    private int linkType;

    /**
     * 是否在菜单栏隐藏：0否1是
     */
    private int isHide;

    /**
     * 菜单权限
     */
    private String perms;
}
