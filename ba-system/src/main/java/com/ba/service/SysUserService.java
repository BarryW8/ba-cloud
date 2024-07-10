package com.ba.service;

import com.ba.base.BaseService;
import com.ba.base.UserInfo;
import com.ba.dto.SysUserRoleDTO;
import com.ba.model.system.SysRole;
import com.ba.model.system.SysUser;
import com.ba.model.system.SysUserRole;

import java.util.List;

public interface SysUserService extends BaseService<SysUser> {
    List<SysUser> findListHasPwd(String sql);

    /**
     * 刷新用户缓存
     */
    UserInfo setUserCache(Long userId);

    /**
     * 删除用户缓存
     */
    void delUserCache(Long userId);

    int saveUserRole(SysUserRoleDTO dto);

    SysRole findUserRoleByAppType(Long userId, Integer appType);
}
