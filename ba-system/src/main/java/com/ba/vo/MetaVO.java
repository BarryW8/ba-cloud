package com.ba.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * title, hideChildren, target, icon, permission
 */
@Data
public class MetaVO implements Serializable {

    /**
     * 页面名称
     */
    private String title;

    /**
     * 是否隐藏
     */
    private boolean hideChildren;

    /**
     * 内外链
     */
    private String target;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单权限
     */
    private String permission;
}
