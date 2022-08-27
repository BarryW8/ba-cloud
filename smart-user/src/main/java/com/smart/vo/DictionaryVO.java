package com.smart.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DictionaryVO implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 父code
     */
    private String parentCode;

    /**
     * 字典编号
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 状态：0-正常，1-禁用
     */
    private int status;

    /**
     * 菜单权限
     */
    private String perms;

    /**
     * 备注
     */
    private String note;

    /**
     * 排序
     */
    private Integer orderBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 修改人
     */
    private Long updateUserId;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 修改人名称
     */
    private String updateUserName;

    private List<DictionaryVO> children = new ArrayList<>();

}
