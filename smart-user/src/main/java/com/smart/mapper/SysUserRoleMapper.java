package com.smart.mapper;


import com.smart.base.BaseMapper;
import com.smart.model.user.SysRole;
import com.smart.model.user.SysUser;
import com.smart.model.user.SysUserRole;
import com.smart.vo.SysUserRoleVO;

import java.util.List;

/**
 * 用户角色信息表
 *
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleVO> {

    int deleteByRoleId(Long roleId);

    int deleteByUserId(Long userId);

    int saveList(List<SysUserRole> list);

}
