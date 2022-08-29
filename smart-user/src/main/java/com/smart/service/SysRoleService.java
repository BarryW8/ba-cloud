package com.smart.service;

import com.smart.base.BaseService;
import com.smart.dto.SysRoleDTO;
import com.smart.dto.SysUserRoleDTO;
import com.smart.model.LoginUser;
import com.smart.model.user.SysRole;
import com.smart.model.user.SysUserRole;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {

    int saveRole(SysRoleDTO dto);

    List<SysUserRole> findByRoleId(Long roleId);

    int saveRoleUser(SysUserRoleDTO dto);

}
