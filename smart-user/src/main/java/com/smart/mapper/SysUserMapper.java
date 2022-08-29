package com.smart.mapper;


import com.smart.base.BaseMapper;
import com.smart.model.user.SysUser;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysUser> findListHasPwd(String sql);
}
