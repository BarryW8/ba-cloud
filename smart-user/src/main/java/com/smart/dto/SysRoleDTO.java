package com.smart.dto;

import com.smart.base.BaseModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysRoleDTO extends BaseModel {


    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态 0启用 1停用
     */
    private int status;

    /**
     * 按钮权限集合
     */
    private List<String> permList = new ArrayList<>();
}
