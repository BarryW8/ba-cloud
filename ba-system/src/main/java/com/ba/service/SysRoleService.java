package com.ba.service;

import com.ba.base.BaseService;
import com.ba.dto.SysRoleDTO;
import com.ba.dto.SysUserRoleDTO;
import com.ba.model.system.SysRole;
import com.ba.model.system.SysRoleMenu;
import com.ba.model.system.SysUserRole;
import com.ba.vo.SysMenuVO;
import com.ba.vo.SysUserRoleVO;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {
//    int saveRoleUser(SysUserRoleDTO dto);
//
//    List<SysUserRole> findRoleUser(Long roleId);

    List<SysRoleMenu> findRoleMenu(Long roleId);

    void refreshCache(SysRole model, List<SysMenuVO> permList);
}
