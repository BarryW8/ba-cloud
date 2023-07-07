package com.ba.service;

import com.ba.base.BaseService;
import com.ba.model.system.SysRoleMenu;

import java.util.List;

/**
 * 角色菜单中间表
 *
 */
public interface SysRoleMenuService extends BaseService<SysRoleMenu> {

    List<SysRoleMenu> findRoleMenu(Long modelId);

}

