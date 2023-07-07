package com.ba.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * web 菜单返回VO
 */
@Data
public class MenuVO implements Serializable {

    private String id;

    private String parentId;
    /**
     * 菜单名称  全局唯一
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件地址 - 文件地址
     */
    private String component;

    /**
     * icon 内外链等
     */
    private MetaVO meta;
}
