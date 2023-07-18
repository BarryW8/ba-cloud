package com.ba.vo;

import com.ba.model.system.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserVO extends SysUser {

    private String roleName;

}
