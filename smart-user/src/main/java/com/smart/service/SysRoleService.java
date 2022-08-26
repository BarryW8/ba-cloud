package com.smart.service;

import com.smart.base.BaseService;
import com.smart.dto.SysRoleDTO;
import com.smart.model.LoginUser;
import com.smart.model.user.SysRole;

public interface SysRoleService extends BaseService<SysRole> {

    int saveRole(SysRoleDTO dto, LoginUser currentUser);

}
