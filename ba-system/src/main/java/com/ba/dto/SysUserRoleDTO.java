package com.ba.dto;

import com.ba.model.system.SysUserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRoleDTO extends SysUserRole {

    /**
     * 应用类型
     */
    private Integer appType;

}
