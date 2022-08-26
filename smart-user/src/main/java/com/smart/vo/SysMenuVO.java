package com.smart.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenuVO implements Serializable {

    /**
     * 主键id
     */
    private String id;

    /**
     * key
     */
    private String key;

    /**
     * 菜单标题
     */
    private String label;


    /**
     * 上级id
     */
    private String parentId;


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

    /**
     * 排序
     */
    private int orderBy;

    /**
     * 创建时间
     */
    private String createTime;

    private List<SysMenuVO> children = new ArrayList<>();

}
