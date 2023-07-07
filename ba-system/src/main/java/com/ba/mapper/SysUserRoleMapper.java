package com.ba.mapper;


import com.ba.base.BaseMapper;
import com.ba.model.system.SysUserRole;
import com.ba.vo.SysUserRoleVO;

import java.util.List;

/**
 * 用户角色信息表
 *
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int deleteByRoleId(Long roleId);

    int deleteByUserId(Long userId);

    int saveList(List<SysUserRole> list);

}
