package com.smart.dto;

import com.smart.base.BaseDTO;
import com.smart.base.BaseModel;
import com.smart.model.LoginUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysRoleDTO extends BaseDTO {

    /**
     * 主键ID
     */
    private Long id;

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
