package com.ba.mapper;


import com.ba.base.BaseMapper;
import com.ba.model.system.SysUser;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysUser> findListHasPwd(String sql);
}
