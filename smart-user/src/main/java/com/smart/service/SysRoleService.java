package com.smart.service;

import com.smart.base.BaseService;
import com.smart.dto.SysRoleDTO;
import com.smart.dto.SysUserRoleDTO;
import com.smart.model.LoginUser;
import com.smart.model.user.SysRole;
import com.smart.model.user.SysUserRole;
import com.smart.vo.SysUserRoleVO;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {

    int saveRole(SysRoleDTO dto);

    int saveRoleUser(SysUserRoleDTO dto);

    List<SysUserRoleVO> findRoleUser(Long roleId);

}
