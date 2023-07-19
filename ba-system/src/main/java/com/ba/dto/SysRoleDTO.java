package com.ba.dto;

import com.ba.model.system.SysRole;
import com.ba.vo.SysMenuVO;
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
    private List<SysMenuVO> permList = new ArrayList<>();

}
