package com.ba.model.system;

import com.ba.base.BaseModel;
import lombok.Data;

@Data
public class SysRole extends BaseModel {


    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态 0启用 1停用
     */
    private int status;

    /**
     * 是否是超管角色，设置一个超管角色就行 0非超管，1超管
     */
    private int isManager;

}
