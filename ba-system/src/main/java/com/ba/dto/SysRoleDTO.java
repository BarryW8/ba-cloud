package com.ba.dto;

import com.ba.model.system.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleDTO extends SysRole {

    /**
     * 按钮权限
     */
    private List<String> permList = new ArrayList<>();

}
