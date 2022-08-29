package com.smart.mapper;


import com.smart.base.BaseMapper;
import com.smart.model.user.SysUserRole;

import java.util.List;

/**
 * 用户角色信息表
 *
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int deleteByRoleId(Long roleId);

    int deleteByUserId(Long userId);

    int saveList(List<SysUserRole> list);

    List<SysUserRole> findByLogin(Long userId);
}
